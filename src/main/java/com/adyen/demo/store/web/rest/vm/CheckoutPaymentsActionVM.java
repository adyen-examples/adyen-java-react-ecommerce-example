package com.adyen.demo.store.web.rest.vm;

import java.util.Map;
import com.adyen.model.Amount;
import com.adyen.model.checkout.CheckoutPaymentsAction;

public class CheckoutPaymentsActionVM {
    private String alternativeReference;

    private Map<String, String> data;

    private String downloadUrl;

    private String expiresAt;

    private Amount initialAmount;

    private String instructionsUrl;

    private String issuer;

    private String maskedTelephoneNumber;

    private String merchantName;

    private String merchantReference;

    private String method;

    private String paymentData;

    private String paymentMethodType;

    private String qrCodeData;

    private String reference;

    private String shopperEmail;

    private String shopperName;

    private Amount surcharge;

    private String token;

    private Amount totalAmount;

    private String entity;

    private String type;

    private String url;

    public CheckoutPaymentsActionVM(final CheckoutPaymentsAction v) {
        this.alternativeReference = v.getAlternativeReference();
        this.data = v.getData();
        this.downloadUrl = v.getDownloadUrl();
        this.expiresAt = v.getExpiresAt();
        this.initialAmount = v.getInitialAmount();
        this.instructionsUrl = v.getInstructionsUrl();
        this.issuer = v.getIssuer();
        this.maskedTelephoneNumber = v.getMaskedTelephoneNumber();
        this.merchantName = v.getMerchantName();
        this.merchantReference = v.getMerchantReference();
        this.method = v.getMethod();
        this.paymentData = v.getPaymentData();
        this.paymentMethodType = v.getPaymentMethodType();
        this.qrCodeData = v.getQrCodeData();
        this.reference = v.getReference();
        this.shopperEmail = v.getShopperEmail();
        this.shopperName = v.getShopperName();
        this.surcharge = v.getSurcharge();
        this.token = v.getToken();
        this.totalAmount = v.getTotalAmount();
        this.entity = v.getEntity();
        this.type = v.getType() != null ? v.getType().getValue() : "";
        this.url = v.getUrl();
    }

    public String getAlternativeReference() {
        return alternativeReference;
    }

    public CheckoutPaymentsActionVM setAlternativeReference(final String alternativeReference) {
        this.alternativeReference = alternativeReference;
        return this;
    }

    public Map<String, String> getData() {
        return data;
    }

    public CheckoutPaymentsActionVM setData(final Map<String, String> data) {
        this.data = data;
        return this;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public CheckoutPaymentsActionVM setDownloadUrl(final String downloadUrl) {
        this.downloadUrl = downloadUrl;
        return this;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public CheckoutPaymentsActionVM setExpiresAt(final String expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public Amount getInitialAmount() {
        return initialAmount;
    }

    public CheckoutPaymentsActionVM setInitialAmount(final Amount initialAmount) {
        this.initialAmount = initialAmount;
        return this;
    }

    public String getInstructionsUrl() {
        return instructionsUrl;
    }

    public CheckoutPaymentsActionVM setInstructionsUrl(final String instructionsUrl) {
        this.instructionsUrl = instructionsUrl;
        return this;
    }

    public String getIssuer() {
        return issuer;
    }

    public CheckoutPaymentsActionVM setIssuer(final String issuer) {
        this.issuer = issuer;
        return this;
    }

    public String getMaskedTelephoneNumber() {
        return maskedTelephoneNumber;
    }

    public CheckoutPaymentsActionVM setMaskedTelephoneNumber(final String maskedTelephoneNumber) {
        this.maskedTelephoneNumber = maskedTelephoneNumber;
        return this;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public CheckoutPaymentsActionVM setMerchantName(final String merchantName) {
        this.merchantName = merchantName;
        return this;
    }

    public String getMerchantReference() {
        return merchantReference;
    }

    public CheckoutPaymentsActionVM setMerchantReference(final String merchantReference) {
        this.merchantReference = merchantReference;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public CheckoutPaymentsActionVM setMethod(final String method) {
        this.method = method;
        return this;
    }

    public String getPaymentData() {
        return paymentData;
    }

    public CheckoutPaymentsActionVM setPaymentData(final String paymentData) {
        this.paymentData = paymentData;
        return this;
    }

    public String getPaymentMethodType() {
        return paymentMethodType;
    }

    public CheckoutPaymentsActionVM setPaymentMethodType(final String paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
        return this;
    }

    public String getQrCodeData() {
        return qrCodeData;
    }

    public CheckoutPaymentsActionVM setQrCodeData(final String qrCodeData) {
        this.qrCodeData = qrCodeData;
        return this;
    }

    public String getReference() {
        return reference;
    }

    public CheckoutPaymentsActionVM setReference(final String reference) {
        this.reference = reference;
        return this;
    }

    public String getShopperEmail() {
        return shopperEmail;
    }

    public CheckoutPaymentsActionVM setShopperEmail(final String shopperEmail) {
        this.shopperEmail = shopperEmail;
        return this;
    }

    public String getShopperName() {
        return shopperName;
    }

    public CheckoutPaymentsActionVM setShopperName(final String shopperName) {
        this.shopperName = shopperName;
        return this;
    }

    public Amount getSurcharge() {
        return surcharge;
    }

    public CheckoutPaymentsActionVM setSurcharge(final Amount surcharge) {
        this.surcharge = surcharge;
        return this;
    }

    public String getToken() {
        return token;
    }

    public CheckoutPaymentsActionVM setToken(final String token) {
        this.token = token;
        return this;
    }

    public Amount getTotalAmount() {
        return totalAmount;
    }

    public CheckoutPaymentsActionVM setTotalAmount(final Amount totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public String getEntity() {
        return entity;
    }

    public CheckoutPaymentsActionVM setEntity(final String entity) {
        this.entity = entity;
        return this;
    }

    public String getType() {
        return type;
    }

    public CheckoutPaymentsActionVM setType(final String type) {
        this.type = type;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public CheckoutPaymentsActionVM setUrl(final String url) {
        this.url = url;
        return this;
    }

    @Override
    public String toString() {
        return "CheckoutPaymentsActionVM{" +
            "alternativeReference='" + alternativeReference + '\'' +
            ", data=" + data +
            ", downloadUrl='" + downloadUrl + '\'' +
            ", expiresAt='" + expiresAt + '\'' +
            ", initialAmount=" + initialAmount +
            ", instructionsUrl='" + instructionsUrl + '\'' +
            ", issuer='" + issuer + '\'' +
            ", maskedTelephoneNumber='" + maskedTelephoneNumber + '\'' +
            ", merchantName='" + merchantName + '\'' +
            ", merchantReference='" + merchantReference + '\'' +
            ", method='" + method + '\'' +
            ", paymentData='" + paymentData + '\'' +
            ", paymentMethodType='" + paymentMethodType + '\'' +
            ", qrCodeData='" + qrCodeData + '\'' +
            ", reference='" + reference + '\'' +
            ", shopperEmail='" + shopperEmail + '\'' +
            ", shopperName='" + shopperName + '\'' +
            ", surcharge=" + surcharge +
            ", token='" + token + '\'' +
            ", totalAmount=" + totalAmount +
            ", entity='" + entity + '\'' +
            ", type='" + type + '\'' +
            ", url='" + url + '\'' +
            '}';
    }
}
