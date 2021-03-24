package com.adyen.demo.store.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentCache.
 */
@Entity
@Table(name = "payment_cache")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PaymentCache implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "order_ref", nullable = false)
    private String orderRef;

    @Column(name = "original_host")
    private String originalHost;

    @Column(name = "payment_data")
    private String paymentData;

    @Column(name = "payment_type")
    private String paymentType;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentCache id(Long id) {
        this.id = id;
        return this;
    }

    public String getOrderRef() {
        return this.orderRef;
    }

    public PaymentCache orderRef(String orderRef) {
        this.orderRef = orderRef;
        return this;
    }

    public void setOrderRef(String orderRef) {
        this.orderRef = orderRef;
    }

    public String getOriginalHost() {
        return this.originalHost;
    }

    public PaymentCache originalHost(String originalHost) {
        this.originalHost = originalHost;
        return this;
    }

    public void setOriginalHost(String originalHost) {
        this.originalHost = originalHost;
    }

    public String getPaymentData() {
        return this.paymentData;
    }

    public PaymentCache paymentData(String paymentData) {
        this.paymentData = paymentData;
        return this;
    }

    public void setPaymentData(String paymentData) {
        this.paymentData = paymentData;
    }

    public String getPaymentType() {
        return this.paymentType;
    }

    public PaymentCache paymentType(String paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public User getUser() {
        return this.user;
    }

    public PaymentCache user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentCache)) {
            return false;
        }
        return id != null && id.equals(((PaymentCache) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCache{" +
            "id=" + getId() +
            ", orderRef='" + getOrderRef() + "'" +
            ", originalHost='" + getOriginalHost() + "'" +
            ", paymentData='" + getPaymentData() + "'" +
            ", paymentType='" + getPaymentType() + "'" +
            "}";
    }
}
