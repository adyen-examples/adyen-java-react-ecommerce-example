package com.adyen.demo.store.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PersistentAuditEvent.class)
public abstract class PersistentAuditEvent_ {

	public static volatile SingularAttribute<PersistentAuditEvent, String> principal;
	public static volatile SingularAttribute<PersistentAuditEvent, Instant> auditEventDate;
	public static volatile MapAttribute<PersistentAuditEvent, String, String> data;
	public static volatile SingularAttribute<PersistentAuditEvent, Long> id;
	public static volatile SingularAttribute<PersistentAuditEvent, String> auditEventType;

	public static final String PRINCIPAL = "principal";
	public static final String AUDIT_EVENT_DATE = "auditEventDate";
	public static final String DATA = "data";
	public static final String ID = "id";
	public static final String AUDIT_EVENT_TYPE = "auditEventType";

}

