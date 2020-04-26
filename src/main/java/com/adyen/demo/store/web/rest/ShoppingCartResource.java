package com.adyen.demo.store.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.adyen.demo.store.domain.ShoppingCart;
import com.adyen.demo.store.security.AuthoritiesConstants;
import com.adyen.demo.store.security.SecurityUtils;
import com.adyen.demo.store.service.ShoppingCartService;
import com.adyen.demo.store.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

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

    public ShoppingCartResource(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
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
        return ResponseEntity.created(new URI("/api/shopping-carts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shopping-carts} : Updates an existing shoppingCart.
     *
     * @param shoppingCart the shoppingCart to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingCart,
     * or with status {@code 400 (Bad Request)} if the shoppingCart is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shoppingCart couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shopping-carts")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ShoppingCart> updateShoppingCart(@Valid @RequestBody ShoppingCart shoppingCart) throws URISyntaxException {
        log.debug("REST request to update ShoppingCart : {}", shoppingCart);
        if (shoppingCart.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShoppingCart result = shoppingCartService.save(shoppingCart);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shoppingCart.getId().toString()))
            .body(result);
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }


    /**
     * {@code GET  /shopping-carts/current-user} : get the active shoppingCart of current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shoppingCart, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shopping-carts/current-user")
    public ResponseEntity<ShoppingCart> getActiveShoppingCartByUser() {
        String user = SecurityUtils.getCurrentUserLogin().orElse("");
        log.debug("REST request to get ShoppingCart for user: {}", user);
        return ResponseEntity.ok().body(shoppingCartService.findActiveCartByUser(user));
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
        String user = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new EntityNotFoundException("User not found"));
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
        String user = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new EntityNotFoundException("User not found"));
        ShoppingCart result = shoppingCartService.removeProductOrderForUser(id, user);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
