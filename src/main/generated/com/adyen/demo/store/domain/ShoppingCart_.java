package com.adyen.demo.store.domain;

import com.adyen.demo.store.domain.enumeration.OrderStatus;
import com.adyen.demo.store.domain.enumeration.PaymentMethod;
import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ShoppingCart.class)
public abstract class ShoppingCart_ {

	public static volatile SingularAttribute<ShoppingCart, BigDecimal> totalPrice;
	public static volatile SingularAttribute<ShoppingCart, String> paymentReference;
	public static volatile SingularAttribute<ShoppingCart, PaymentMethod> paymentMethod;
	public static volatile SetAttribute<ShoppingCart, ProductOrder> orders;
	public static volatile SingularAttribute<ShoppingCart, Long> id;
	public static volatile SingularAttribute<ShoppingCart, CustomerDetails> customerDetails;
	public static volatile SingularAttribute<ShoppingCart, Instant> placedDate;
	public static volatile SingularAttribute<ShoppingCart, OrderStatus> status;

	public static final String TOTAL_PRICE = "totalPrice";
	public static final String PAYMENT_REFERENCE = "paymentReference";
	public static final String PAYMENT_METHOD = "paymentMethod";
	public static final String ORDERS = "orders";
	public static final String ID = "id";
	public static final String CUSTOMER_DETAILS = "customerDetails";
	public static final String PLACED_DATE = "placedDate";
	public static final String STATUS = "status";

}

