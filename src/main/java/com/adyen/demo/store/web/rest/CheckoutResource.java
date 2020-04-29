package com.adyen.demo.store.web.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import com.adyen.Client;
import com.adyen.demo.store.domain.ShoppingCart;
import com.adyen.demo.store.security.SecurityUtils;
import com.adyen.demo.store.service.ShoppingCartService;
import com.adyen.demo.store.web.rest.vm.PaymentRedirectVM;
import com.adyen.demo.store.web.rest.vm.PaymentRequestVM;
import com.adyen.demo.store.web.rest.vm.PaymentResponseVM;
import com.adyen.enums.Environment;
import com.adyen.model.Amount;
import com.adyen.model.checkout.*;
import com.adyen.service.Checkout;
import com.adyen.service.exception.ApiException;

/**
 * REST controller for using Adyen checkout API
 */
@RestController
@RequestMapping("/api")
public class CheckoutResource {
    private final Logger log = LoggerFactory.getLogger(CheckoutResource.class);
    private static final String ORIGINAL_HOST_COOKIE = "originalHost";
    private static final String PAYMENT_TYPE_COOKIE = "paymentType";
    private static final String PAYMENT_DATA_COOKIE = "paymentData";

    @Value("${ADYEN_MERCHANT_ACCOUNT}")
    private String merchantAccount;
    @Value("${ADYEN_ORIGIN_KEY}")
    private String originKey;

    private final Checkout checkout;
    private final ShoppingCartService shoppingCartService;

    public CheckoutResource(final ShoppingCartService shoppingCartService, @Value("${ADYEN_API_KEY}") String apiKey) {
        this.shoppingCartService = shoppingCartService;
        Client client = new Client(apiKey, Environment.TEST);
        this.checkout = new Checkout(client);
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
        conf.put("originKey", originKey);
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
    public ResponseEntity<PaymentMethodsResponse> paymentMethods() throws EntityNotFoundException, IOException, ApiException {
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
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} and with body the paymentMethods response.
     * @throws EntityNotFoundException when user is not found.
     * @throws IOException             from Adyen API.
     * @throws ApiException            from Adyen API.
     */
    @PostMapping("/checkout/initiate-payment")
    public ResponseEntity<PaymentResponseVM> payments(@RequestHeader("referer") String referer, @RequestBody PaymentRequestVM body, HttpServletResponse httpResponse) throws EntityNotFoundException, IOException, ApiException {
        PaymentsRequest paymentRequest = new PaymentsRequest();
        paymentRequest.setMerchantAccount(merchantAccount);
        paymentRequest.setCountryCode("NL");
        paymentRequest.setShopperLocale("nl-NL");
        paymentRequest.setChannel(PaymentsRequest.ChannelEnum.WEB);
        URL refURL = new URL(referer);
        String originalHost = refURL.getProtocol() + "://" + refURL.getHost() + (refURL.getPort() != 0 ? ":" + refURL.getPort() : "");
        String returnUrl = originalHost + "/api/checkout/redirect";
        paymentRequest.setReturnUrl(returnUrl);
        paymentRequest.setReference(Instant.now().toString());
        paymentRequest.setPaymentMethod(body.getPaymentMethod());
        paymentRequest.setBrowserInfo(body.getBrowserInfo());
        paymentRequest.setOrigin(body.getOrigin());

        Amount amount = getAmountFromCart();
        paymentRequest.setAmount(amount);
        log.debug("REST request to make Adyen payment {}", paymentRequest);
        PaymentResponseVM response = new PaymentResponseVM(checkout.payments(paymentRequest));
        if (response.getAction() != null && !response.getAction().getPaymentData().isEmpty()) {
            Cookie cookie = new Cookie(PAYMENT_DATA_COOKIE, response.getAction().getPaymentData());
            cookie.setMaxAge(3600);
            cookie.setHttpOnly(true);
            httpResponse.addCookie(cookie);
            cookie = new Cookie(ORIGINAL_HOST_COOKIE, referer);
            cookie.setMaxAge(3600);
            httpResponse.addCookie(cookie);
            cookie = new Cookie(PAYMENT_TYPE_COOKIE, body.getPaymentMethod().getType());
            cookie.setMaxAge(3600);
            httpResponse.addCookie(cookie);
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
    public ResponseEntity<PaymentResponseVM> payments(@RequestBody PaymentsDetailsRequest detailsRequest) throws IOException, ApiException {
        log.debug("REST request to make Adyen payment details {}", detailsRequest);
        PaymentsResponse paymentsResponse = checkout.paymentsDetails(detailsRequest);
        PaymentResponseVM response = new PaymentResponseVM(paymentsResponse);
        return ResponseEntity.ok()
            .body(response);
    }

    /**
     * {@code GET  /checkout/redirect} : Handle redirect during payment.
     *
     * @return the {@link RedirectView} with status {@code 302}
     * @throws IOException  from Adyen API.
     * @throws ApiException from Adyen API.
     */
    @GetMapping("/checkout/redirect")
    public RedirectView redirect(@RequestParam("payload") String payload, @CookieValue(PAYMENT_DATA_COOKIE) String paymentData, @CookieValue(ORIGINAL_HOST_COOKIE) String originalHost, @CookieValue("paymentType") String paymentType) throws IOException, ApiException {
        PaymentsDetailsRequest detailsRequest = new PaymentsDetailsRequest();
        detailsRequest.setDetails(Collections.singletonMap("payload", payload));
        detailsRequest.setPaymentData(paymentData);

        return getRedirectView(originalHost, paymentType, detailsRequest);
    }

    /**
     * {@code POST  /checkout/redirect} : Handle redirect during payment.
     *
     * @return the {@link RedirectView} with status {@code 302}
     * @throws IOException  from Adyen API.
     * @throws ApiException from Adyen API.
     */
    @PostMapping("/checkout/redirect")
    public RedirectView redirect(@RequestBody PaymentRedirectVM payload, @CookieValue(PAYMENT_DATA_COOKIE) String paymentData, @CookieValue(ORIGINAL_HOST_COOKIE) String originalHost, @CookieValue("paymentType") String paymentType) throws IOException, ApiException {
        PaymentsDetailsRequest detailsRequest = new PaymentsDetailsRequest();
        Map<String, String> details = new HashMap<>();
        details.put("MD", payload.getMD());
        details.put("PaRes", payload.getPaRes());
        detailsRequest.setDetails(details);
        detailsRequest.setPaymentData(paymentData);
        return getRedirectView(originalHost, paymentType, detailsRequest);
    }

    private RedirectView getRedirectView(final String originalHost, final String paymentType, final PaymentsDetailsRequest detailsRequest) throws ApiException, IOException {
        log.debug("REST request to handle payment redirect {}", detailsRequest);
        PaymentsResponse paymentsResponse = checkout.paymentsDetails(detailsRequest);
        PaymentResponseVM response = new PaymentResponseVM(paymentsResponse);
        String redirectURL = originalHost + "/status/";
        switch (paymentsResponse.getResultCode()) {
            case AUTHORISED:
                redirectURL += "success";
                break;
            case PENDING:
            case RECEIVED:
                redirectURL += "pending";
                break;
            case REFUSED:
                redirectURL += "failed";
                break;
            default:
                redirectURL += "error";
                break;
        }
        return new RedirectView(redirectURL + "?reason=" + response.getResultCode() + "&paymentType=" + paymentType);
    }

    private Amount getAmountFromCart() {
        String user = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new EntityNotFoundException("User not found"));
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
