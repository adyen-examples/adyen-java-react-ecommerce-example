package com.adyen.demo.store.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductOrder.class)
public abstract class ProductOrder_ {

	public static volatile SingularAttribute<ProductOrder, Product> product;
	public static volatile SingularAttribute<ProductOrder, Integer> quantity;
	public static volatile SingularAttribute<ProductOrder, BigDecimal> totalPrice;
	public static volatile SingularAttribute<ProductOrder, Long> id;
	public static volatile SingularAttribute<ProductOrder, ShoppingCart> cart;

	public static final String PRODUCT = "product";
	public static final String QUANTITY = "quantity";
	public static final String TOTAL_PRICE = "totalPrice";
	public static final String ID = "id";
	public static final String CART = "cart";

}

