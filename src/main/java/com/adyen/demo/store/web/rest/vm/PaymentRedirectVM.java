package com.adyen.demo.store.web.rest.vm;

public class PaymentRedirectVM {
    private String paRes;
    private String MD;

    public String getPaRes() {
        return paRes;
    }

    public PaymentRedirectVM setPaRes(final String paRes) {
        this.paRes = paRes;
        return this;
    }

    public String getMD() {
        return MD;
    }

    public PaymentRedirectVM setMD(final String MD) {
        this.MD = MD;
        return this;
    }

    @Override
    public String toString() {
        return "PaymentRedirectVM{" +
            "paRes='" + paRes + '\'' +
            ", MD='" + MD + '\'' +
            '}';
    }
}
