package com.adyen.demo.store.repository;

import com.adyen.demo.store.domain.PaymentCache;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the PaymentCache entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentCacheRepository extends JpaRepository<PaymentCache, Long> {

    @Query("select paymentCache from PaymentCache paymentCache where paymentCache.user.login = ?#{principal.username}")
    List<PaymentCache> findByUserIsCurrentUser();
}
