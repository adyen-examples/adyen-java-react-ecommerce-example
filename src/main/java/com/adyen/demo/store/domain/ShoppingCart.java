package com.adyen.demo.store.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.adyen.demo.store.domain.enumeration.OrderStatus;
import com.adyen.demo.store.domain.enumeration.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A ShoppingCart.
 */
@Entity
@Table(name = "shopping_cart")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "placed_date", nullable = false)
    private Instant placedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "payment_modification_reference")
    private String paymentModificationReference;

    @OneToMany(mappedBy = "cart")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ProductOrder> orders = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "carts", allowSetters = true)
    private CustomerDetails customerDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getPlacedDate() {
        return placedDate;
    }

    public ShoppingCart placedDate(Instant placedDate) {
        this.placedDate = placedDate;
        return this;
    }

    public void setPlacedDate(Instant placedDate) {
        this.placedDate = placedDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public ShoppingCart status(OrderStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public ShoppingCart totalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void calculateTotalPrice() {
        if (null != this.orders) {
            this.setTotalPrice(this.orders.stream().map(ProductOrder::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        }
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public ShoppingCart paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public ShoppingCart paymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
        return this;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getPaymentModificationReference() {
        return paymentModificationReference;
    }

    public ShoppingCart paymentModificationReference(String paymentModificationReference) {
        this.paymentModificationReference = paymentModificationReference;
        return this;
    }

    public void setPaymentModificationReference(String paymentModificationReference) {
        this.paymentModificationReference = paymentModificationReference;
    }

    public Set<ProductOrder> getOrders() {
        return orders;
    }

    public ShoppingCart orders(Set<ProductOrder> productOrders) {
        this.orders = productOrders;
        calculateTotalPrice();
        return this;
    }

    public ShoppingCart addOrder(ProductOrder productOrder) {
        this.orders.add(productOrder);
        productOrder.setCart(this);
        calculateTotalPrice();
        return this;
    }

    public ShoppingCart removeOrder(ProductOrder productOrder) {
        this.orders.remove(productOrder);
        productOrder.setCart(null);
        calculateTotalPrice();
        return this;
    }

    public void setOrders(Set<ProductOrder> productOrders) {
        this.orders = productOrders;
        calculateTotalPrice();
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public ShoppingCart customerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
        return this;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


    public ShoppingCart() {
    }

    public ShoppingCart(@NotNull final Instant placedDate, @NotNull final OrderStatus status, @NotNull @DecimalMin(value = "0") final BigDecimal totalPrice, @NotNull final PaymentMethod paymentMethod, @NotNull final CustomerDetails customerDetails) {
        this.placedDate = placedDate;
        this.status = status;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.customerDetails = customerDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShoppingCart)) {
            return false;
        }
        return id != null && id.equals(((ShoppingCart) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShoppingCart{" +
            "id=" + getId() +
            ", placedDate='" + getPlacedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", paymentReference='" + getPaymentReference() + "'" +
            ", paymentModificationReference='" + getPaymentModificationReference() + "'" +
            "}";
    }
}
