package com.adyen.demo.store.web.rest;

import com.adyen.demo.store.domain.CustomerDetails;
import com.adyen.demo.store.repository.CustomerDetailsRepository;
import com.adyen.demo.store.service.CustomerDetailsService;
import com.adyen.demo.store.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.adyen.demo.store.domain.CustomerDetails}.
 */
@RestController
@RequestMapping("/api")
public class CustomerDetailsResource {

    private final Logger log = LoggerFactory.getLogger(CustomerDetailsResource.class);

    private static final String ENTITY_NAME = "customerDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerDetailsService customerDetailsService;

    private final CustomerDetailsRepository customerDetailsRepository;

    public CustomerDetailsResource(CustomerDetailsService customerDetailsService, CustomerDetailsRepository customerDetailsRepository) {
        this.customerDetailsService = customerDetailsService;
        this.customerDetailsRepository = customerDetailsRepository;
    }

    /**
     * {@code POST  /customer-details} : Create a new customerDetails.
     *
     * @param customerDetails the customerDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerDetails, or with status {@code 400 (Bad Request)} if the customerDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-details")
    public ResponseEntity<CustomerDetails> createCustomerDetails(@Valid @RequestBody CustomerDetails customerDetails)
        throws URISyntaxException {
        log.debug("REST request to save CustomerDetails : {}", customerDetails);
        if (customerDetails.getId() != null) {
            throw new BadRequestAlertException("A new customerDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerDetails result = customerDetailsService.save(customerDetails);
        return ResponseEntity
            .created(new URI("/api/customer-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-details/:id} : Updates an existing customerDetails.
     *
     * @param id the id of the customerDetails to save.
     * @param customerDetails the customerDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerDetails,
     * or with status {@code 400 (Bad Request)} if the customerDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-details/{id}")
    public ResponseEntity<CustomerDetails> updateCustomerDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerDetails customerDetails
    ) throws URISyntaxException {
        log.debug("REST request to update CustomerDetails : {}, {}", id, customerDetails);
        if (customerDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerDetails result = customerDetailsService.save(customerDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customer-details/:id} : Partial updates given fields of an existing customerDetails, field will ignore if it is null
     *
     * @param id the id of the customerDetails to save.
     * @param customerDetails the customerDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerDetails,
     * or with status {@code 400 (Bad Request)} if the customerDetails is not valid,
     * or with status {@code 404 (Not Found)} if the customerDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/customer-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CustomerDetails> partialUpdateCustomerDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerDetails customerDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomerDetails partially : {}, {}", id, customerDetails);
        if (customerDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerDetails> result = customerDetailsService.partialUpdate(customerDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-details} : get all the customerDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerDetails in body.
     */
    @GetMapping("/customer-details")
    public ResponseEntity<List<CustomerDetails>> getAllCustomerDetails(Pageable pageable) {
        log.debug("REST request to get a page of CustomerDetails");
        Page<CustomerDetails> page = customerDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-details/:id} : get the "id" customerDetails.
     *
     * @param id the id of the customerDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-details/{id}")
    public ResponseEntity<CustomerDetails> getCustomerDetails(@PathVariable Long id) {
        log.debug("REST request to get CustomerDetails : {}", id);
        Optional<CustomerDetails> customerDetails = customerDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerDetails);
    }

    /**
     * {@code DELETE  /customer-details/:id} : delete the "id" customerDetails.
     *
     * @param id the id of the customerDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-details/{id}")
    public ResponseEntity<Void> deleteCustomerDetails(@PathVariable Long id) {
        log.debug("REST request to delete CustomerDetails : {}", id);
        customerDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
