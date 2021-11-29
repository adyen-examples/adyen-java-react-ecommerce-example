package com.adyen.demo.store.web.rest;

import com.adyen.demo.store.domain.ProductCategory;
import com.adyen.demo.store.repository.ProductCategoryRepository;
import com.adyen.demo.store.service.ProductCategoryService;
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
 * REST controller for managing {@link com.adyen.demo.store.domain.ProductCategory}.
 */
@RestController
@RequestMapping("/api")
public class ProductCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ProductCategoryResource.class);

    private static final String ENTITY_NAME = "productCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductCategoryService productCategoryService;

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryResource(ProductCategoryService productCategoryService, ProductCategoryRepository productCategoryRepository) {
        this.productCategoryService = productCategoryService;
        this.productCategoryRepository = productCategoryRepository;
    }

    /**
     * {@code POST  /product-categories} : Create a new productCategory.
     *
     * @param productCategory the productCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productCategory, or with status {@code 400 (Bad Request)} if the productCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-categories")
    public ResponseEntity<ProductCategory> createProductCategory(@Valid @RequestBody ProductCategory productCategory)
        throws URISyntaxException {
        log.debug("REST request to save ProductCategory : {}", productCategory);
        if (productCategory.getId() != null) {
            throw new BadRequestAlertException("A new productCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductCategory result = productCategoryService.save(productCategory);
        return ResponseEntity
            .created(new URI("/api/product-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-categories/:id} : Updates an existing productCategory.
     *
     * @param id the id of the productCategory to save.
     * @param productCategory the productCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productCategory,
     * or with status {@code 400 (Bad Request)} if the productCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-categories/{id}")
    public ResponseEntity<ProductCategory> updateProductCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductCategory productCategory
    ) throws URISyntaxException {
        log.debug("REST request to update ProductCategory : {}, {}", id, productCategory);
        if (productCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductCategory result = productCategoryService.save(productCategory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-categories/:id} : Partial updates given fields of an existing productCategory, field will ignore if it is null
     *
     * @param id the id of the productCategory to save.
     * @param productCategory the productCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productCategory,
     * or with status {@code 400 (Bad Request)} if the productCategory is not valid,
     * or with status {@code 404 (Not Found)} if the productCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the productCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-categories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductCategory> partialUpdateProductCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductCategory productCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductCategory partially : {}, {}", id, productCategory);
        if (productCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductCategory> result = productCategoryService.partialUpdate(productCategory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /product-categories} : get all the productCategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productCategories in body.
     */
    @GetMapping("/product-categories")
    public ResponseEntity<List<ProductCategory>> getAllProductCategories(Pageable pageable) {
        log.debug("REST request to get a page of ProductCategories");
        Page<ProductCategory> page = productCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-categories/:id} : get the "id" productCategory.
     *
     * @param id the id of the productCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-categories/{id}")
    public ResponseEntity<ProductCategory> getProductCategory(@PathVariable Long id) {
        log.debug("REST request to get ProductCategory : {}", id);
        Optional<ProductCategory> productCategory = productCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productCategory);
    }

    /**
     * {@code DELETE  /product-categories/:id} : delete the "id" productCategory.
     *
     * @param id the id of the productCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-categories/{id}")
    public ResponseEntity<Void> deleteProductCategory(@PathVariable Long id) {
        log.debug("REST request to delete ProductCategory : {}", id);
        productCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
