package com.adyen.demo.store.web.rest;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.adyen.demo.store.domain.ShoppingCart;
import com.adyen.demo.store.domain.enumeration.OrderStatus;
import com.adyen.demo.store.repository.ShoppingCartRepository;
import com.adyen.demo.store.security.AuthoritiesConstants;
import com.adyen.demo.store.security.SecurityUtils;
import com.adyen.demo.store.service.ShoppingCartService;
import com.adyen.demo.store.web.rest.errors.BadRequestAlertException;
import com.adyen.demo.store.web.rest.errors.EntityNotFoundException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.adyen.demo.store.domain.ShoppingCart}.
 */
@RestController
@RequestMapping("/api")
public class ShoppingCartResource {

    private final Logger log = LoggerFactory.getLogger(ShoppingCartResource.class);

    private static final String ENTITY_NAME = "shoppingCart";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShoppingCartService shoppingCartService;

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartResource(ShoppingCartService shoppingCartService, ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartService = shoppingCartService;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    /**
     * {@code POST  /shopping-carts} : Create a new shoppingCart.
     *
     * @param shoppingCart the shoppingCart to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shoppingCart, or with status {@code 400 (Bad Request)} if the shoppingCart has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shopping-carts")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ShoppingCart> createShoppingCart(@Valid @RequestBody ShoppingCart shoppingCart) throws URISyntaxException {
        log.debug("REST request to save ShoppingCart : {}", shoppingCart);
        if (shoppingCart.getId() != null) {
            throw new BadRequestAlertException("A new shoppingCart cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShoppingCart result = shoppingCartService.save(shoppingCart);
        return ResponseEntity
            .created(new URI("/api/shopping-carts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shopping-carts/:id} : Updates an existing shoppingCart.
     *
     * @param id           the id of the shoppingCart to save.
     * @param shoppingCart the shoppingCart to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingCart,
     * or with status {@code 400 (Bad Request)} if the shoppingCart is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shoppingCart couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shopping-carts/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ShoppingCart> updateShoppingCart(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShoppingCart shoppingCart
    ) throws URISyntaxException {
        log.debug("REST request to update ShoppingCart : {}, {}", id, shoppingCart);
        if (shoppingCart.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shoppingCart.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shoppingCartRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShoppingCart result = shoppingCartService.save(shoppingCart);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shoppingCart.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /shopping-carts/:id} : Partial updates given fields of an existing shoppingCart, field will ignore if it is null
     *
     * @param id           the id of the shoppingCart to save.
     * @param shoppingCart the shoppingCart to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingCart,
     * or with status {@code 400 (Bad Request)} if the shoppingCart is not valid,
     * or with status {@code 404 (Not Found)} if the shoppingCart is not found,
     * or with status {@code 500 (Internal Server Error)} if the shoppingCart couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shopping-carts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ShoppingCart> partialUpdateShoppingCart(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShoppingCart shoppingCart
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShoppingCart partially : {}, {}", id, shoppingCart);
        if (shoppingCart.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shoppingCart.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shoppingCartRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShoppingCart> result = shoppingCartService.partialUpdate(shoppingCart);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shoppingCart.getId().toString())
        );
    }

    /**
     * {@code GET  /shopping-carts} : get all the shoppingCarts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shoppingCarts in body.
     */
    @GetMapping("/shopping-carts")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<ShoppingCart> getAllShoppingCarts() {
        log.debug("REST request to get all ShoppingCarts");
        return shoppingCartService.findAll();
    }

    /**
     * {@code GET  /shopping-carts/:id} : get the "id" shoppingCart.
     *
     * @param id the id of the shoppingCart to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shoppingCart, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shopping-carts/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ShoppingCart> getShoppingCart(@PathVariable Long id) {
        log.debug("REST request to get ShoppingCart : {}", id);
        Optional<ShoppingCart> shoppingCart = shoppingCartService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shoppingCart);
    }

    /**
     * {@code DELETE  /shopping-carts/:id} : delete the "id" shoppingCart.
     *
     * @param id the id of the shoppingCart to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shopping-carts/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteShoppingCart(@PathVariable Long id) {
        log.debug("REST request to delete ShoppingCart : {}", id);
        shoppingCartService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }


    /**
     * {@code GET  /shopping-carts/current-user} : get the active shoppingCart of current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shoppingCart, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shopping-carts/current-user-active")
    public ResponseEntity<ShoppingCart> getActiveShoppingCartByUser() {
        String user = SecurityUtils.getCurrentUserLogin().orElse("");
        log.debug("REST request to get ShoppingCart for user: {}", user);
        return ResponseEntity.ok().body(shoppingCartService.findActiveCartByUser(user));
    }

    /**
     * {@code GET  /shopping-carts/current-user} : get all shoppingCarts of current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shoppingCart, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shopping-carts/current-user")
    public List<ShoppingCart> getShoppingCartByUser() {
        String user = SecurityUtils.getCurrentUserLogin().orElse("");
        log.debug("REST request to get ShoppingCart for user: {}", user);
        return shoppingCartService.findCartsByUser(user);
    }


    /**
     * {@code PUT /shopping-carts/add-product/:id} : Add a product to active shoppingCart of current user
     *
     * @param id the id of the product to add.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingCart,
     * or with status {@code 400 (Bad Request)} if the shoppingCart is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shoppingCart couldn't be updated.
     * @throws EntityNotFoundException if the product is not found.
     */
    @PutMapping("/shopping-carts/add-product/{id}")
    public ResponseEntity<ShoppingCart> addProduct(@PathVariable Long id) throws EntityNotFoundException {
        log.debug("REST request to add product to ShoppingCart");
        String user = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new EntityNotFoundException("User"));
        ShoppingCart result = shoppingCartService.addProductForUser(id, user);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code DELETE /shopping-carts/remove-order/:id} : Remove a product order from active shoppingCart of current user
     *
     * @param id the id of the product order to remove.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingCart,
     * or with status {@code 400 (Bad Request)} if the shoppingCart is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shoppingCart couldn't be updated.
     * @throws EntityNotFoundException if the order is not found.
     */
    @DeleteMapping("/shopping-carts/remove-order/{id}")
    public ResponseEntity<ShoppingCart> removeOrder(@PathVariable Long id) throws EntityNotFoundException {
        log.debug("REST request to remove product order from ShoppingCart");
        String user = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new EntityNotFoundException("User"));
        ShoppingCart result = shoppingCartService.removeProductOrderForUser(id, user);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /close} : Close an existing shoppingCart.
     *
     * @param paymentType the payment type used.
     * @param paymentRef  the payment reference from PSP.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingCart,
     * or with status {@code 400 (Bad Request)} if the shoppingCart is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shoppingCart couldn't be updated.
     * @throws EntityNotFoundException if the order is not found.
     */
    @PutMapping("/shopping-carts/close")
    public ResponseEntity<ShoppingCart> closeShoppingCart(@RequestParam String paymentType, @RequestParam String paymentRef, @RequestParam OrderStatus status) throws EntityNotFoundException {
        log.debug("REST request to update ShoppingCart");
        String user = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new EntityNotFoundException("User"));
        ShoppingCart result = shoppingCartService.updateCartForUser(user, paymentType, paymentRef, status);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
