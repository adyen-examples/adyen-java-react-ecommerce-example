package com.adyen.demo.store.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.adyen.demo.store.domain.CustomerDetails;
import com.adyen.demo.store.domain.Product;
import com.adyen.demo.store.domain.ProductOrder;
import com.adyen.demo.store.domain.ShoppingCart;
import com.adyen.demo.store.domain.enumeration.OrderStatus;
import com.adyen.demo.store.domain.enumeration.PaymentMethod;
import com.adyen.demo.store.repository.CustomerDetailsRepository;
import com.adyen.demo.store.repository.ShoppingCartRepository;

/**
 * Service Implementation for managing {@link ShoppingCart}.
 */
@Service
@Transactional
public class ShoppingCartService {

    private final Logger log = LoggerFactory.getLogger(ShoppingCartService.class);

    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;
    private final CustomerDetailsRepository customerDetailsRepository;
    private final ProductOrderService productOrderService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService, final CustomerDetailsRepository customerDetailsRepository, final ProductOrderService productOrderService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
        this.customerDetailsRepository = customerDetailsRepository;
        this.productOrderService = productOrderService;
    }

    /**
     * Save a shoppingCart.
     *
     * @param shoppingCart the entity to save.
     * @return the persisted entity.
     */
    public ShoppingCart save(ShoppingCart shoppingCart) {
        log.debug("Request to save ShoppingCart : {}", shoppingCart);
        return shoppingCartRepository.save(shoppingCart);
    }

    /**
     * Get all the shoppingCarts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ShoppingCart> findAll() {
        log.debug("Request to get all ShoppingCarts");
        return shoppingCartRepository.findAll();
    }

    /**
     * Get one shoppingCart by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> findOne(Long id) {
        log.debug("Request to get ShoppingCart : {}", id);
        return shoppingCartRepository.findById(id);
    }

    /**
     * Delete the shoppingCart by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ShoppingCart : {}", id);
        shoppingCartRepository.deleteById(id);
    }

    public Optional<ShoppingCart> findActiveCartByUser(String user) {
        Optional<ShoppingCart> oCart = shoppingCartRepository.findFirstByCustomerDetailsUserLoginAndStatusOrderByIdAsc(user, OrderStatus.PENDING);
        // also serves as lazy init of orders
        oCart.ifPresent(cart -> log.info("Cart for user {} has {} orders", user, cart.getOrders().size()));
        return oCart;
    }

    public ShoppingCart addProductForUser(Long id, String user) throws EntityNotFoundException {
        Optional<ShoppingCart> carts = findActiveCartByUser(user);
        ShoppingCart activeCart = carts.orElseGet(() -> {
            Optional<CustomerDetails> customer = customerDetailsRepository.findOneByUserLogin(user);
            return shoppingCartRepository.save(new ShoppingCart(
                Instant.now(), OrderStatus.PENDING, BigDecimal.ZERO, PaymentMethod.CREDIT_CARD, customer.get()
            ));
        });
        Product product = productService.findOne(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        ProductOrder order;
        List<ProductOrder> orders = activeCart.getOrders().stream().filter(productOrder -> productOrder.getProduct().getId().equals(id)).collect(Collectors.toList());
        if (orders.isEmpty()) {
            order = new ProductOrder();
            order.setQuantity(1);
            order.setTotalPrice(product.getPrice());
            order.setProduct(product);
            order.setCart(activeCart);
            activeCart.addOrder(order);
        } else {
            order = orders.get(0);
            order.setQuantity(order.getQuantity() + 1);
            order.setTotalPrice(product.getPrice().multiply(new BigDecimal(order.getQuantity())));
        }
        productOrderService.save(order);
        return shoppingCartRepository.save(activeCart);
    }

    public ShoppingCart removeProductOrderForUser(final Long id, final String user) {
        Optional<ShoppingCart> carts = findActiveCartByUser(user);
        ShoppingCart activeCart = carts.orElseThrow(() -> new EntityNotFoundException("Shopping cart not found"));
        List<ProductOrder> orders = activeCart.getOrders().stream().filter(productOrder -> productOrder.getId().equals(id)).collect(Collectors.toList());
        if (orders.isEmpty()) {
            throw new EntityNotFoundException("Product order not found in cart");
        } else {
            ProductOrder order = orders.get(0);
            activeCart.removeOrder(order);
            productOrderService.delete(order.getId());
        }
        return shoppingCartRepository.save(activeCart);
    }
}
