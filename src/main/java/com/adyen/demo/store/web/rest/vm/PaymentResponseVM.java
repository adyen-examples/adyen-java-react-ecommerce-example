package com.adyen.demo.store.web.rest.vm;

import java.util.List;
import java.util.Map;
import com.adyen.model.FraudResult;
import com.adyen.model.checkout.*;
import com.adyen.model.ThreeDS2Result;

public class PaymentResponseVM {
    private Map<String, String> additionalData;

    private List<InputDetail> details;

    private FraudResult fraudResult;

    private String paymentData;

    private String pspReference;

    private Redirect redirect;

    private String refusalReason;

    private String refusalReasonCode;

    private String resultCode;

    private ServiceError serviceError;

    private String authResponse;

    private String merchantReference;

    private Map<String, String> outputDetails;

    private Map<String, String> authentication;

    private ThreeDS2Result threeDS2Result;

    private CheckoutPaymentsActionVM action;

    public Map<String, String> getAdditionalData() {
        return additionalData;
    }

    public PaymentResponseVM setAdditionalData(final Map<String, String> additionalData) {
        this.additionalData = additionalData;
        return this;
    }

    public List<InputDetail> getDetails() {
        return details;
    }

    public PaymentResponseVM setDetails(final List<InputDetail> details) {
        this.details = details;
        return this;
    }

    public FraudResult getFraudResult() {
        return fraudResult;
    }

    public PaymentResponseVM setFraudResult(final FraudResult fraudResult) {
        this.fraudResult = fraudResult;
        return this;
    }

    public String getPaymentData() {
        return paymentData;
    }

    public PaymentResponseVM setPaymentData(final String paymentData) {
        this.paymentData = paymentData;
        return this;
    }

    public String getPspReference() {
        return pspReference;
    }

    public PaymentResponseVM setPspReference(final String pspReference) {
        this.pspReference = pspReference;
        return this;
    }

    public Redirect getRedirect() {
        return redirect;
    }

    public PaymentResponseVM setRedirect(final Redirect redirect) {
        this.redirect = redirect;
        return this;
    }

    public String getRefusalReason() {
        return refusalReason;
    }

    public PaymentResponseVM setRefusalReason(final String refusalReason) {
        this.refusalReason = refusalReason;
        return this;
    }

    public String getRefusalReasonCode() {
        return refusalReasonCode;
    }

    public PaymentResponseVM setRefusalReasonCode(final String refusalReasonCode) {
        this.refusalReasonCode = refusalReasonCode;
        return this;
    }

    public String getResultCode() {
        return resultCode;
    }

    public PaymentResponseVM setResultCode(final String resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public ServiceError getServiceError() {
        return serviceError;
    }

    public PaymentResponseVM setServiceError(final ServiceError serviceError) {
        this.serviceError = serviceError;
        return this;
    }

    public String getAuthResponse() {
        return authResponse;
    }

    public PaymentResponseVM setAuthResponse(final String authResponse) {
        this.authResponse = authResponse;
        return this;
    }

    public String getMerchantReference() {
        return merchantReference;
    }

    public PaymentResponseVM setMerchantReference(final String merchantReference) {
        this.merchantReference = merchantReference;
        return this;
    }

    public Map<String, String> getOutputDetails() {
        return outputDetails;
    }

    public PaymentResponseVM setOutputDetails(final Map<String, String> outputDetails) {
        this.outputDetails = outputDetails;
        return this;
    }

    public Map<String, String> getAuthentication() {
        return authentication;
    }

    public PaymentResponseVM setAuthentication(final Map<String, String> authentication) {
        this.authentication = authentication;
        return this;
    }

    public ThreeDS2Result getThreeDS2Result() {
        return threeDS2Result;
    }

    public PaymentResponseVM setThreeDS2Result(final ThreeDS2Result threeDS2Result) {
        this.threeDS2Result = threeDS2Result;
        return this;
    }

    public CheckoutPaymentsActionVM getAction() {
        return action;
    }

    public PaymentResponseVM setAction(final CheckoutPaymentsActionVM action) {
        this.action = action;
        return this;
    }

    public PaymentResponseVM(final PaymentsResponse v) {
        this.additionalData = v.getAdditionalData();
        this.details = v.getDetails();
        this.fraudResult = v.getFraudResult();
        this.paymentData = v.getPaymentData();
        this.pspReference = v.getPspReference();
        this.redirect = v.getRedirect();
        this.refusalReason = v.getRefusalReason();
        this.refusalReasonCode = v.getRefusalReasonCode();
        this.resultCode = v.getResultCode() != null ? v.getResultCode().getValue() : "";
        this.serviceError = v.getServiceError();
        this.authResponse = v.getAuthResponse() != null ? v.getAuthResponse().getValue() : "";
        this.merchantReference = v.getMerchantReference();
        this.outputDetails = v.getOutputDetails();
        this.authentication = v.getAuthentication();
        this.threeDS2Result = v.getThreeDS2Result();
        this.action = v.getAction() != null ? new CheckoutPaymentsActionVM(v.getAction()) : null;
    }
}
