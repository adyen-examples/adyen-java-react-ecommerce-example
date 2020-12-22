package com.adyen.demo.store.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.adyen.demo.store.domain.ShoppingCart;
import com.adyen.demo.store.domain.enumeration.OrderStatus;

/**
 * Spring Data  repository for the ShoppingCart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    Optional<ShoppingCart> findFirstByCustomerDetailsUserLoginAndStatusOrderByIdAsc(String login, OrderStatus orderStatus);

    List<ShoppingCart> findAllByCustomerDetailsUserLoginAndStatusNot(String user, OrderStatus orderStatus);

    Optional<ShoppingCart> findOneByPaymentModificationReference(String paymentRef);

    Optional<ShoppingCart> findOneByPaymentReference(String paymentRef);
}
