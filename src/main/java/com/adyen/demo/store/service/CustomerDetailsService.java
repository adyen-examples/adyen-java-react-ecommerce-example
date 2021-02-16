package com.adyen.demo.store.service;

import com.adyen.demo.store.domain.CustomerDetails;
import com.adyen.demo.store.repository.CustomerDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CustomerDetails}.
 */
@Service
@Transactional
public class CustomerDetailsService {

    private final Logger log = LoggerFactory.getLogger(CustomerDetailsService.class);

    private final CustomerDetailsRepository customerDetailsRepository;

    public CustomerDetailsService(CustomerDetailsRepository customerDetailsRepository) {
        this.customerDetailsRepository = customerDetailsRepository;
    }

    /**
     * Save a customerDetails.
     *
     * @param customerDetails the entity to save.
     * @return the persisted entity.
     */
    public CustomerDetails save(CustomerDetails customerDetails) {
        log.debug("Request to save CustomerDetails : {}", customerDetails);
        return customerDetailsRepository.save(customerDetails);
    }

    /**
     * Get all the customerDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerDetails> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerDetails");
        return customerDetailsRepository.findAll(pageable);
    }

    /**
     * Get one customerDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerDetails> findOne(Long id) {
        log.debug("Request to get CustomerDetails : {}", id);
        return customerDetailsRepository.findById(id);
    }

    /**
     * Delete the customerDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomerDetails : {}", id);
        customerDetailsRepository.deleteById(id);
    }
}
