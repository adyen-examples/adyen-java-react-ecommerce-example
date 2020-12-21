package com.adyen.demo.store.web.rest;

import com.adyen.demo.store.StoreApp;
import com.adyen.demo.store.domain.ShoppingCart;
import com.adyen.demo.store.domain.CustomerDetails;
import com.adyen.demo.store.repository.ShoppingCartRepository;
import com.adyen.demo.store.service.ShoppingCartService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adyen.demo.store.domain.enumeration.OrderStatus;
import com.adyen.demo.store.domain.enumeration.PaymentMethod;
/**
 * Integration tests for the {@link ShoppingCartResource} REST controller.
 */
@SpringBootTest(classes = StoreApp.class)

@AutoConfigureMockMvc
@WithMockUser(username="admin", authorities={"ROLE_ADMIN"}, password = "admin")
public class ShoppingCartResourceIT {

    private static final Instant DEFAULT_PLACED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PLACED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.COMPLETED;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.PAID;

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(1);

    private static final PaymentMethod DEFAULT_PAYMENT_METHOD = PaymentMethod.CREDIT_CARD;
    private static final PaymentMethod UPDATED_PAYMENT_METHOD = PaymentMethod.IDEAL;

    private static final String DEFAULT_PAYMENT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_MODIFICATION_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_MODIFICATION_REFERENCE = "BBBBBBBBBB";

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShoppingCartMockMvc;

    private ShoppingCart shoppingCart;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingCart createEntity(EntityManager em) {
        ShoppingCart shoppingCart = new ShoppingCart()
            .placedDate(DEFAULT_PLACED_DATE)
            .status(DEFAULT_STATUS)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .paymentReference(DEFAULT_PAYMENT_REFERENCE)
            .paymentModificationReference(DEFAULT_PAYMENT_MODIFICATION_REFERENCE);
        // Add required entity
        CustomerDetails customerDetails;
        if (TestUtil.findAll(em, CustomerDetails.class).isEmpty()) {
            customerDetails = CustomerDetailsResourceIT.createEntity(em);
            em.persist(customerDetails);
            em.flush();
        } else {
            customerDetails = TestUtil.findAll(em, CustomerDetails.class).get(0);
        }
        shoppingCart.setCustomerDetails(customerDetails);
        return shoppingCart;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingCart createUpdatedEntity(EntityManager em) {
        ShoppingCart shoppingCart = new ShoppingCart()
            .placedDate(UPDATED_PLACED_DATE)
            .status(UPDATED_STATUS)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .paymentModificationReference(UPDATED_PAYMENT_MODIFICATION_REFERENCE);
        // Add required entity
        CustomerDetails customerDetails;
        if (TestUtil.findAll(em, CustomerDetails.class).isEmpty()) {
            customerDetails = CustomerDetailsResourceIT.createUpdatedEntity(em);
            em.persist(customerDetails);
            em.flush();
        } else {
            customerDetails = TestUtil.findAll(em, CustomerDetails.class).get(0);
        }
        shoppingCart.setCustomerDetails(customerDetails);
        return shoppingCart;
    }

    @BeforeEach
    public void initTest() {
        shoppingCart = createEntity(em);
    }

    @Test
    @Transactional
    public void createShoppingCart() throws Exception {
        int databaseSizeBeforeCreate = shoppingCartRepository.findAll().size();

        // Create the ShoppingCart
        restShoppingCartMockMvc.perform(post("/api/shopping-carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shoppingCart)))
            .andExpect(status().isCreated());

        // Validate the ShoppingCart in the database
        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
        assertThat(shoppingCartList).hasSize(databaseSizeBeforeCreate + 1);
        ShoppingCart testShoppingCart = shoppingCartList.get(shoppingCartList.size() - 1);
        assertThat(testShoppingCart.getPlacedDate()).isEqualTo(DEFAULT_PLACED_DATE);
        assertThat(testShoppingCart.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testShoppingCart.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testShoppingCart.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testShoppingCart.getPaymentReference()).isEqualTo(DEFAULT_PAYMENT_REFERENCE);
        assertThat(testShoppingCart.getPaymentModificationReference()).isEqualTo(DEFAULT_PAYMENT_MODIFICATION_REFERENCE);
    }

    @Test
    @Transactional
    public void createShoppingCartWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shoppingCartRepository.findAll().size();

        // Create the ShoppingCart with an existing ID
        shoppingCart.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShoppingCartMockMvc.perform(post("/api/shopping-carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shoppingCart)))
            .andExpect(status().isBadRequest());

        // Validate the ShoppingCart in the database
        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
        assertThat(shoppingCartList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPlacedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = shoppingCartRepository.findAll().size();
        // set the field null
        shoppingCart.setPlacedDate(null);

        // Create the ShoppingCart, which fails.

        restShoppingCartMockMvc.perform(post("/api/shopping-carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shoppingCart)))
            .andExpect(status().isBadRequest());

        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
        assertThat(shoppingCartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = shoppingCartRepository.findAll().size();
        // set the field null
        shoppingCart.setStatus(null);

        // Create the ShoppingCart, which fails.

        restShoppingCartMockMvc.perform(post("/api/shopping-carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shoppingCart)))
            .andExpect(status().isBadRequest());

        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
        assertThat(shoppingCartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = shoppingCartRepository.findAll().size();
        // set the field null
        shoppingCart.setTotalPrice(null);

        // Create the ShoppingCart, which fails.

        restShoppingCartMockMvc.perform(post("/api/shopping-carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shoppingCart)))
            .andExpect(status().isBadRequest());

        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
        assertThat(shoppingCartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentMethodIsRequired() throws Exception {
        int databaseSizeBeforeTest = shoppingCartRepository.findAll().size();
        // set the field null
        shoppingCart.setPaymentMethod(null);

        // Create the ShoppingCart, which fails.

        restShoppingCartMockMvc.perform(post("/api/shopping-carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shoppingCart)))
            .andExpect(status().isBadRequest());

        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
        assertThat(shoppingCartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShoppingCarts() throws Exception {
        // Initialize the database
        shoppingCartRepository.saveAndFlush(shoppingCart);

        // Get all the shoppingCartList
        restShoppingCartMockMvc.perform(get("/api/shopping-carts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingCart.getId().intValue())))
            .andExpect(jsonPath("$.[*].placedDate").value(hasItem(DEFAULT_PLACED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].paymentReference").value(hasItem(DEFAULT_PAYMENT_REFERENCE)))
            .andExpect(jsonPath("$.[*].paymentModificationReference").value(hasItem(DEFAULT_PAYMENT_MODIFICATION_REFERENCE)));
    }

    @Test
    @Transactional
    public void getShoppingCart() throws Exception {
        // Initialize the database
        shoppingCartRepository.saveAndFlush(shoppingCart);

        // Get the shoppingCart
        restShoppingCartMockMvc.perform(get("/api/shopping-carts/{id}", shoppingCart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shoppingCart.getId().intValue()))
            .andExpect(jsonPath("$.placedDate").value(DEFAULT_PLACED_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.intValue()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()))
            .andExpect(jsonPath("$.paymentReference").value(DEFAULT_PAYMENT_REFERENCE))
            .andExpect(jsonPath("$.paymentModificationReference").value(DEFAULT_PAYMENT_MODIFICATION_REFERENCE));
    }

    @Test
    @Transactional
    public void getNonExistingShoppingCart() throws Exception {
        // Get the shoppingCart
        restShoppingCartMockMvc.perform(get("/api/shopping-carts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShoppingCart() throws Exception {
        // Initialize the database
        shoppingCartService.save(shoppingCart);

        int databaseSizeBeforeUpdate = shoppingCartRepository.findAll().size();

        // Update the shoppingCart
        ShoppingCart updatedShoppingCart = shoppingCartRepository.findById(shoppingCart.getId()).get();
        // Disconnect from session so that the updates on updatedShoppingCart are not directly saved in db
        em.detach(updatedShoppingCart);
        updatedShoppingCart
            .placedDate(UPDATED_PLACED_DATE)
            .status(UPDATED_STATUS)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .paymentModificationReference(UPDATED_PAYMENT_MODIFICATION_REFERENCE);

        restShoppingCartMockMvc.perform(put("/api/shopping-carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedShoppingCart)))
            .andExpect(status().isOk());

        // Validate the ShoppingCart in the database
        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
        assertThat(shoppingCartList).hasSize(databaseSizeBeforeUpdate);
        ShoppingCart testShoppingCart = shoppingCartList.get(shoppingCartList.size() - 1);
        assertThat(testShoppingCart.getPlacedDate()).isEqualTo(UPDATED_PLACED_DATE);
        assertThat(testShoppingCart.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testShoppingCart.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testShoppingCart.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testShoppingCart.getPaymentReference()).isEqualTo(UPDATED_PAYMENT_REFERENCE);
        assertThat(testShoppingCart.getPaymentModificationReference()).isEqualTo(UPDATED_PAYMENT_MODIFICATION_REFERENCE);
    }

    @Test
    @Transactional
    public void updateNonExistingShoppingCart() throws Exception {
        int databaseSizeBeforeUpdate = shoppingCartRepository.findAll().size();

        // Create the ShoppingCart

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingCartMockMvc.perform(put("/api/shopping-carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shoppingCart)))
            .andExpect(status().isBadRequest());

        // Validate the ShoppingCart in the database
        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
        assertThat(shoppingCartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShoppingCart() throws Exception {
        // Initialize the database
        shoppingCartService.save(shoppingCart);

        int databaseSizeBeforeDelete = shoppingCartRepository.findAll().size();

        // Delete the shoppingCart
        restShoppingCartMockMvc.perform(delete("/api/shopping-carts/{id}", shoppingCart.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
        assertThat(shoppingCartList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
