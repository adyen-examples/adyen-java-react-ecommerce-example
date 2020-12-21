package com.adyen.demo.store.domain;

import com.adyen.demo.store.domain.enumeration.Gender;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CustomerDetails.class)
public abstract class CustomerDetails_ {

	public static volatile SingularAttribute<CustomerDetails, String> country;
	public static volatile SetAttribute<CustomerDetails, ShoppingCart> carts;
	public static volatile SingularAttribute<CustomerDetails, Gender> gender;
	public static volatile SingularAttribute<CustomerDetails, String> phone;
	public static volatile SingularAttribute<CustomerDetails, String> city;
	public static volatile SingularAttribute<CustomerDetails, String> addressLine1;
	public static volatile SingularAttribute<CustomerDetails, String> addressLine2;
	public static volatile SingularAttribute<CustomerDetails, Long> id;
	public static volatile SingularAttribute<CustomerDetails, User> user;

	public static final String COUNTRY = "country";
	public static final String CARTS = "carts";
	public static final String GENDER = "gender";
	public static final String PHONE = "phone";
	public static final String CITY = "city";
	public static final String ADDRESS_LINE1 = "addressLine1";
	public static final String ADDRESS_LINE2 = "addressLine2";
	public static final String ID = "id";
	public static final String USER = "user";

}

