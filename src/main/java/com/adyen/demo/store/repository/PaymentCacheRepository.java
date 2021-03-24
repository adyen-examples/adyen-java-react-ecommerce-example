package com.adyen.demo.store.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.adyen.demo.store.domain.PaymentCache;

/**
 * Spring Data SQL repository for the PaymentCache entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentCacheRepository extends JpaRepository<PaymentCache, Long> {
    @Query("select paymentCache from PaymentCache paymentCache where paymentCache.user.login = ?#{principal.username}")
    List<PaymentCache> findByUserIsCurrentUser();

    Optional<PaymentCache> findOneByOrderRef(String orderRef);
}
