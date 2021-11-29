package com.adyen.demo.store.service;

import com.adyen.demo.store.domain.ProductOrder;
import com.adyen.demo.store.repository.ProductOrderRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductOrder}.
 */
@Service
@Transactional
public class ProductOrderService {

    private final Logger log = LoggerFactory.getLogger(ProductOrderService.class);

    private final ProductOrderRepository productOrderRepository;

    public ProductOrderService(ProductOrderRepository productOrderRepository) {
        this.productOrderRepository = productOrderRepository;
    }

    /**
     * Save a productOrder.
     *
     * @param productOrder the entity to save.
     * @return the persisted entity.
     */
    public ProductOrder save(ProductOrder productOrder) {
        log.debug("Request to save ProductOrder : {}", productOrder);
        return productOrderRepository.save(productOrder);
    }

    /**
     * Partially update a productOrder.
     *
     * @param productOrder the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductOrder> partialUpdate(ProductOrder productOrder) {
        log.debug("Request to partially update ProductOrder : {}", productOrder);

        return productOrderRepository
            .findById(productOrder.getId())
            .map(
                existingProductOrder -> {
                    if (productOrder.getQuantity() != null) {
                        existingProductOrder.setQuantity(productOrder.getQuantity());
                    }
                    if (productOrder.getTotalPrice() != null) {
                        existingProductOrder.setTotalPrice(productOrder.getTotalPrice());
                    }

                    return existingProductOrder;
                }
            )
            .map(productOrderRepository::save);
    }

    /**
     * Get all the productOrders.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductOrder> findAll() {
        log.debug("Request to get all ProductOrders");
        return productOrderRepository.findAll();
    }

    /**
     * Get one productOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductOrder> findOne(Long id) {
        log.debug("Request to get ProductOrder : {}", id);
        return productOrderRepository.findById(id);
    }

    /**
     * Delete the productOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductOrder : {}", id);
        productOrderRepository.deleteById(id);
    }
}
