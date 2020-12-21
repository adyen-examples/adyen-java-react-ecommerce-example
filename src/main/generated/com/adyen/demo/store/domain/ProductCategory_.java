package com.adyen.demo.store.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductCategory.class)
public abstract class ProductCategory_ {

	public static volatile SingularAttribute<ProductCategory, String> name;
	public static volatile SingularAttribute<ProductCategory, String> description;
	public static volatile SingularAttribute<ProductCategory, Long> id;
	public static volatile SetAttribute<ProductCategory, Product> products;

	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String PRODUCTS = "products";

}

