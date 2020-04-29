package com.adyen.demo.store.web.rest.vm;

import javax.validation.constraints.NotNull;
import com.adyen.model.BrowserInfo;
import com.adyen.model.checkout.DefaultPaymentMethodDetails;

public class PaymentRequestVM {
    @NotNull
    private DefaultPaymentMethodDetails paymentMethod;
    private BrowserInfo browserInfo;
    private String origin;

    public DefaultPaymentMethodDetails getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentRequestVM setPaymentMethod(final DefaultPaymentMethodDetails paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public BrowserInfo getBrowserInfo() {
        return browserInfo;
    }

    public PaymentRequestVM setBrowserInfo(final BrowserInfo browserInfo) {
        this.browserInfo = browserInfo;
        return this;
    }

    public String getOrigin() {
        return origin;
    }

    public PaymentRequestVM setOrigin(final String origin) {
        this.origin = origin;
        return this;
    }

    @Override
    public String toString() {
        return "PaymentRequestDTO{" +
            "paymentMethod=" + paymentMethod +
            ", browserInfo=" + browserInfo +
            ", origin='" + origin + '\'' +
            '}';
    }
}
