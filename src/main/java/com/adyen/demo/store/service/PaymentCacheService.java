package com.adyen.demo.store.service;

import com.adyen.demo.store.domain.PaymentCache;
import com.adyen.demo.store.repository.PaymentCacheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PaymentCache}.
 */
@Service
@Transactional
public class PaymentCacheService {

    private final Logger log = LoggerFactory.getLogger(PaymentCacheService.class);

    private final PaymentCacheRepository paymentCacheRepository;

    public PaymentCacheService(PaymentCacheRepository paymentCacheRepository) {
        this.paymentCacheRepository = paymentCacheRepository;
    }

    /**
     * Save a paymentCache.
     *
     * @param paymentCache the entity to save.
     * @return the persisted entity.
     */
    public PaymentCache save(PaymentCache paymentCache) {
        log.debug("Request to save PaymentCache : {}", paymentCache);
        return paymentCacheRepository.save(paymentCache);
    }

    /**
     * Get all the paymentCaches.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentCache> findAll() {
        log.debug("Request to get all PaymentCaches");
        return paymentCacheRepository.findAll();
    }

    /**
     * Get one paymentCache by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentCache> findOne(Long id) {
        log.debug("Request to get PaymentCache : {}", id);
        return paymentCacheRepository.findById(id);
    }

    /**
     * Delete the paymentCache by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PaymentCache : {}", id);
        paymentCacheRepository.deleteById(id);
    }
}
