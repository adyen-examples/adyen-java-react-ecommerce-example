package com.adyen.demo.store.web.rest;

import java.security.SignatureException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.adyen.demo.store.domain.ShoppingCart;
import com.adyen.demo.store.domain.enumeration.OrderStatus;
import com.adyen.demo.store.service.ShoppingCartService;
import com.adyen.demo.store.web.rest.errors.EntityNotFoundException;
import com.adyen.model.notification.NotificationRequest;
import com.adyen.model.notification.NotificationRequestItem;
import com.adyen.notification.NotificationHandler;
import com.adyen.util.HMACValidator;

@RestController
@RequestMapping("/api")
public class WebhookResource {
    private final Logger log = LoggerFactory.getLogger(WebhookResource.class);

    private final HMACValidator hmacValidator = new HMACValidator();
    @Value("${ADYEN_HMAC_KEY}")
    private String hmacKey;

    private ShoppingCartService shoppingCartService;

    public WebhookResource(final ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/webhook/notification")
    public ResponseEntity<String> notification(@RequestBody String req) throws SignatureException {
        NotificationHandler notificationHandler = new NotificationHandler();
        NotificationRequest notificationRequest = notificationHandler.handleNotificationJson(req);
        // Handling multiple notificationRequests
        List<NotificationRequestItem> notificationRequestItems = notificationRequest.getNotificationItems();
        for (NotificationRequestItem item : notificationRequestItems) {
            // Process the notification based on the eventCode
            switch (item.getEventCode()) {
                // for Ideal pending payments
                case NotificationRequestItem.EVENT_CODE_PENDING:
                    log.info("Payment pending for reference {}", item.getPspReference());
                    break;
                case NotificationRequestItem.EVENT_CODE_AUTHORISATION: {
                    ShoppingCart cart = shoppingCartService.findOneByPaymentReference(item.getPspReference()).orElseThrow(() -> new EntityNotFoundException("Shopping cart"));
                    if (item.isSuccess()) {
                        cart.setStatus(OrderStatus.PAID);
                    } else {
                        cart.setStatus(OrderStatus.CANCELLED);
                    }
                    shoppingCartService.save(cart);
                    break;
                }
                case NotificationRequestItem.EVENT_CODE_CANCEL_OR_REFUND:
                    if (hmacValidator.validateHMAC(item, hmacKey)) {
                        ShoppingCart cart = shoppingCartService.findOneByPaymentModificationReference(item.getPspReference()).orElseThrow(() -> new EntityNotFoundException("Shopping cart"));
                        if (item.isSuccess()) {
                            // update DB with additionalData.modification.action
                            if (item.getAdditionalData().containsKey("modification.action") && "refund".equals(item.getAdditionalData().get("modification.action"))) {
                                cart.setStatus(OrderStatus.REFUNDED);
                            } else {
                                cart.setStatus(OrderStatus.CANCELLED);
                            }
                        } else {
                            // update DB with failure
                            cart.setStatus(OrderStatus.REFUND_FAILED);
                        }
                        // update cart
                        shoppingCartService.save(cart);
                    } else {
                        log.error("NotificationRequest with invalid HMAC key received");
                    }
                    break;
                default:
                    // do nothing
                    log.info("skipping non actionable webhook");
            }
        }
        return ResponseEntity.ok()
            .

                body("[accepted]");
    }
}
