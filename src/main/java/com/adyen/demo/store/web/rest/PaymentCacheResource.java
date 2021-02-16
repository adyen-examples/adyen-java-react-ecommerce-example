package com.adyen.demo.store.web.rest;

import com.adyen.demo.store.domain.PaymentCache;
import com.adyen.demo.store.service.PaymentCacheService;
import com.adyen.demo.store.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.adyen.demo.store.domain.PaymentCache}.
 */
@RestController
@RequestMapping("/api")
public class PaymentCacheResource {

    private final Logger log = LoggerFactory.getLogger(PaymentCacheResource.class);

    private static final String ENTITY_NAME = "paymentCache";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentCacheService paymentCacheService;

    public PaymentCacheResource(PaymentCacheService paymentCacheService) {
        this.paymentCacheService = paymentCacheService;
    }

    /**
     * {@code POST  /payment-caches} : Create a new paymentCache.
     *
     * @param paymentCache the paymentCache to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentCache, or with status {@code 400 (Bad Request)} if the paymentCache has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-caches")
    public ResponseEntity<PaymentCache> createPaymentCache(@Valid @RequestBody PaymentCache paymentCache) throws URISyntaxException {
        log.debug("REST request to save PaymentCache : {}", paymentCache);
        if (paymentCache.getId() != null) {
            throw new BadRequestAlertException("A new paymentCache cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentCache result = paymentCacheService.save(paymentCache);
        return ResponseEntity.created(new URI("/api/payment-caches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-caches} : Updates an existing paymentCache.
     *
     * @param paymentCache the paymentCache to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentCache,
     * or with status {@code 400 (Bad Request)} if the paymentCache is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentCache couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-caches")
    public ResponseEntity<PaymentCache> updatePaymentCache(@Valid @RequestBody PaymentCache paymentCache) throws URISyntaxException {
        log.debug("REST request to update PaymentCache : {}", paymentCache);
        if (paymentCache.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaymentCache result = paymentCacheService.save(paymentCache);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentCache.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payment-caches} : get all the paymentCaches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentCaches in body.
     */
    @GetMapping("/payment-caches")
    public List<PaymentCache> getAllPaymentCaches() {
        log.debug("REST request to get all PaymentCaches");
        return paymentCacheService.findAll();
    }

    /**
     * {@code GET  /payment-caches/:id} : get the "id" paymentCache.
     *
     * @param id the id of the paymentCache to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentCache, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-caches/{id}")
    public ResponseEntity<PaymentCache> getPaymentCache(@PathVariable Long id) {
        log.debug("REST request to get PaymentCache : {}", id);
        Optional<PaymentCache> paymentCache = paymentCacheService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentCache);
    }

    /**
     * {@code DELETE  /payment-caches/:id} : delete the "id" paymentCache.
     *
     * @param id the id of the paymentCache to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-caches/{id}")
    public ResponseEntity<Void> deletePaymentCache(@PathVariable Long id) {
        log.debug("REST request to delete PaymentCache : {}", id);
        paymentCacheService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
