package com.adyen.demo.store.web.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import com.adyen.Client;
import com.adyen.demo.store.domain.PaymentCache;
import com.adyen.demo.store.domain.ShoppingCart;
import com.adyen.demo.store.domain.User;
import com.adyen.demo.store.domain.enumeration.OrderStatus;
import com.adyen.demo.store.repository.UserRepository;
import com.adyen.demo.store.security.SecurityUtils;
import com.adyen.demo.store.service.PaymentCacheService;
import com.adyen.demo.store.service.ShoppingCartService;
import com.adyen.demo.store.web.rest.errors.EntityNotFoundException;
import com.adyen.demo.store.web.rest.vm.PaymentRedirectVM;
import com.adyen.enums.Environment;
import com.adyen.model.Amount;
import com.adyen.model.checkout.*;
import com.adyen.model.modification.CancelOrRefundRequest;
import com.adyen.model.modification.ModificationResult;
import com.adyen.service.Checkout;
import com.adyen.service.Modification;
import com.adyen.service.exception.ApiException;

/**
 * REST controller for using Adyen checkout API
 */
@RestController
@RequestMapping("/api")
public class CheckoutResource {
    private final Logger log = LoggerFactory.getLogger(CheckoutResource.class);

    @Value("${ADYEN_MERCHANT_ACCOUNT}")
    private String merchantAccount;
    @Value("${ADYEN_CLIENT_KEY}")
    private String clientKey;

    private final Checkout checkout;
    private final Modification modification;
    private final ShoppingCartService shoppingCartService;
    private final PaymentCacheService paymentCacheService;
    private final UserRepository userRepository;

    public CheckoutResource(final ShoppingCartService shoppingCartService,
                            final PaymentCacheService paymentCacheService,
                            final UserRepository userRepository,
                            @Value("${ADYEN_API_KEY}") String apiKey) {
        this.shoppingCartService = shoppingCartService;
        this.paymentCacheService = paymentCacheService;
        this.userRepository = userRepository;
        Client client = new Client(apiKey, Environment.TEST);
        this.checkout = new Checkout(client);
        this.modification = new Modification(client);
    }

    /**
     * {@code GET  /checkout/config} : Get secure config
     *
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} and with body the config map.
     */
    @GetMapping("/checkout/config")
    public ResponseEntity<Map> config() {
        Map<String, String> conf = new HashMap<>();
        conf.put("environment", "test");
        conf.put("clientKey", clientKey);
        return ResponseEntity.ok()
            .body(conf);
    }

    /**
     * {@code POST  /checkout/payment-methods} : Get valid payment methods.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} and with body the paymentMethods response.
     * @throws EntityNotFoundException when user is not found.
     * @throws IOException             from Adyen API.
     * @throws ApiException            from Adyen API.
     */
    @PostMapping("/checkout/payment-methods")
    public ResponseEntity<PaymentMethodsResponse> paymentMethods() throws IOException, ApiException {
        PaymentMethodsRequest paymentMethodsRequest = new PaymentMethodsRequest();
        paymentMethodsRequest.setMerchantAccount(merchantAccount);
        paymentMethodsRequest.setCountryCode("NL");
        paymentMethodsRequest.setShopperLocale("nl-NL");
        paymentMethodsRequest.setChannel(PaymentMethodsRequest.ChannelEnum.WEB);

        Amount amount = getAmountFromCart();
        paymentMethodsRequest.setAmount(amount);
        log.debug("REST request to get Adyen payment methods {}", paymentMethodsRequest);
        PaymentMethodsResponse response = checkout.paymentMethods(paymentMethodsRequest);
        return ResponseEntity.ok()
            .body(response);
    }

    /**
     * {@code POST  /checkout/initiate-payment} : Make a payment.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} and with req the paymentMethods response.
     * @throws EntityNotFoundException when user is not found.
     * @throws IOException             from Adyen API.
     * @throws ApiException            from Adyen API.
     */
    @PostMapping("/checkout/initiate-payment")
    public ResponseEntity<PaymentsResponse> payments(@RequestHeader("referer") String referer, @RequestBody PaymentsRequest req, HttpServletRequest request) throws IOException, ApiException {
        PaymentsRequest paymentRequest = new PaymentsRequest();
        paymentRequest.setMerchantAccount(merchantAccount);
        paymentRequest.setCountryCode("NL");
        paymentRequest.setShopperLocale("nl-NL");
        paymentRequest.setChannel(PaymentsRequest.ChannelEnum.WEB);
        URL refURL = new URL(referer);
        String originalHost = refURL.getProtocol() + "://" + refURL.getHost() + (refURL.getPort() >= 80 ? ":" + refURL.getPort() : "");
        String orderRef = UUID.randomUUID().toString();
        String returnUrl = originalHost + "/api/checkout/redirect?orderRef=" + orderRef;
        paymentRequest.setReturnUrl(returnUrl);
        paymentRequest.setReference(orderRef);
        paymentRequest.setPaymentMethod(req.getPaymentMethod());
        // required for 3ds2 native flow
        paymentRequest.setAdditionalData(Collections.singletonMap("allow3DS2", "true"));
        paymentRequest.setOrigin(req.getOrigin());
        // required for 3ds2 flow
        paymentRequest.setBrowserInfo(req.getBrowserInfo());
        // required by some issuers for 3ds2
        paymentRequest.setShopperIP(request.getRemoteAddr());

        Amount amount = getAmountFromCart();
        paymentRequest.setAmount(amount);
        log.debug("REST request to make Adyen payment {}", paymentRequest);
        PaymentsResponse response = checkout.payments(paymentRequest);
        if (response.getAction() != null && !response.getAction().getPaymentData().isEmpty()) {
            String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new EntityNotFoundException("User"));
            User user = userRepository.findOneByLogin(login).orElseThrow(() -> new EntityNotFoundException("User"));
            paymentCacheService.save(
                new PaymentCache()
                    .orderRef(orderRef)
                    .paymentData(response.getAction().getPaymentData())
                    .paymentType(req.getPaymentMethod().getType())
                    .originalHost(referer)
                    .user(user)
            );
        }
        return ResponseEntity.ok()
            .body(response);
    }

    /**
     * {@code POST  /checkout/submit-additional-details} : Make a payment.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} and with body the paymentMethods response.
     * @throws EntityNotFoundException when user is not found.
     * @throws IOException             from Adyen API.
     * @throws ApiException            from Adyen API.
     */
    @PostMapping("/checkout/submit-additional-details")
    public ResponseEntity<PaymentsResponse> payments(@RequestBody PaymentsDetailsRequest detailsRequest) throws IOException, ApiException {
        log.debug("REST request to make Adyen payment details {}", detailsRequest);
        PaymentsResponse response = checkout.paymentsDetails(detailsRequest);
        return ResponseEntity.ok()
            .body(response);
    }

    /**
     * {@code POST  /checkout/refund-payment} : Cancel & Refund a payment.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} and with body the paymentMethods response.
     * @throws EntityNotFoundException when user is not found.
     * @throws IOException             from Adyen API.
     * @throws ApiException            from Adyen API.
     */
    @PostMapping("/checkout/refund-payment")
    public ResponseEntity<ModificationResult> refund(@RequestBody ShoppingCart cart) throws IOException, ApiException {
        log.debug("REST request to cancel payment {}", cart);
        CancelOrRefundRequest req = new CancelOrRefundRequest();
        req.setMerchantAccount(merchantAccount);
        req.setOriginalReference(cart.getPaymentReference());
        req.setReference(UUID.randomUUID().toString());
        ModificationResult modificationResult = modification.cancelOrRefund(req);
        // update the shopping cart with ref & status
        cart = shoppingCartService.findOne(cart.getId()).orElseThrow(() -> new EntityNotFoundException("Cart"));
        cart.setStatus(OrderStatus.REFUND_INITIATED);
        cart.setPaymentModificationReference(modificationResult.getPspReference());
        shoppingCartService.save(cart);

        return ResponseEntity.ok()
            .body(modificationResult);
    }

    /**
     * {@code GET  /checkout/redirect} : Handle redirect during payment.
     *
     * @return the {@link RedirectView} with status {@code 302}
     * @throws IOException  from Adyen API.
     * @throws ApiException from Adyen API.
     */
    @GetMapping("/checkout/redirect")
    public RedirectView redirect(@RequestParam("payload") String payload, @RequestParam String orderRef) throws IOException, ApiException {
        PaymentsDetailsRequest detailsRequest = new PaymentsDetailsRequest();
        detailsRequest.setDetails(Collections.singletonMap("payload", payload));
        return getRedirectView(orderRef, detailsRequest);
    }

    /**
     * {@code POST  /checkout/redirect} : Handle redirect during payment.
     *
     * @return the {@link RedirectView} with status {@code 302}
     * @throws IOException  from Adyen API.
     * @throws ApiException from Adyen API.
     */
    @PostMapping(
        path = "/checkout/redirect",
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public RedirectView redirect(PaymentRedirectVM payload, @RequestParam String orderRef) throws IOException, ApiException {
        PaymentsDetailsRequest detailsRequest = new PaymentsDetailsRequest();
        Map<String, String> details = new HashMap<>();
        details.put("MD", payload.getMD());
        details.put("PaRes", payload.getPaRes());
        detailsRequest.setDetails(details);
        return getRedirectView(orderRef, detailsRequest);
    }

    private RedirectView getRedirectView(final String orderRef, final PaymentsDetailsRequest detailsRequest) throws ApiException, IOException {
        log.debug("REST request to handle payment redirect {}", detailsRequest);
        PaymentCache paymentCache = paymentCacheService.findOneByOrderRef(orderRef).orElseThrow(() -> new EntityNotFoundException("PaymentData"));
        detailsRequest.setPaymentData(paymentCache.getPaymentData());

        PaymentsResponse response = checkout.paymentsDetails(detailsRequest);
        String redirectURL = paymentCache.getOriginalHost() + "/status/";
        switch (response.getResultCode()) {
            case AUTHORISED:
                shoppingCartService.updateCartForUser(paymentCache.getUser().getLogin(), paymentCache.getPaymentType(), response.getPspReference(), OrderStatus.PAID);
                redirectURL += "success";
                break;
            case PENDING:
            case RECEIVED:
                shoppingCartService.updateCartForUser(paymentCache.getUser().getLogin(), paymentCache.getPaymentType(), response.getPspReference(), OrderStatus.PENDING);
                redirectURL += "pending";
                break;
            case REFUSED:
                redirectURL += "failed";
                break;
            default:
                redirectURL += "error";
                break;
        }
        paymentCacheService.delete(paymentCache.getId());
        return new RedirectView(redirectURL + "?reason=" + response.getResultCode());
    }

    private Amount getAmountFromCart() {
        String user = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new EntityNotFoundException("User"));
        ShoppingCart activeCart = shoppingCartService.findActiveCartByUser(user);

        Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setValue(toMinorUnit(activeCart.getTotalPrice()));
        return amount;
    }

    private Long toMinorUnit(BigDecimal val) {
        return val.longValue() * 100L;
    }
}
