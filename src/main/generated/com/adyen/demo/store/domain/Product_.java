package com.adyen.demo.store.domain;

import com.adyen.demo.store.domain.enumeration.Size;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Product.class)
public abstract class Product_ {

	public static volatile SingularAttribute<Product, byte[]> image;
	public static volatile SingularAttribute<Product, Size> size;
	public static volatile SingularAttribute<Product, BigDecimal> price;
	public static volatile SingularAttribute<Product, String> imageContentType;
	public static volatile SingularAttribute<Product, String> name;
	public static volatile SingularAttribute<Product, String> description;
	public static volatile SingularAttribute<Product, Long> id;
	public static volatile SingularAttribute<Product, ProductCategory> productCategory;

	public static final String IMAGE = "image";
	public static final String SIZE = "size";
	public static final String PRICE = "price";
	public static final String IMAGE_CONTENT_TYPE = "imageContentType";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String PRODUCT_CATEGORY = "productCategory";

}

