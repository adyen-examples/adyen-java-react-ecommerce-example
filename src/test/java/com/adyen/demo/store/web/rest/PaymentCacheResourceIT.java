package com.adyen.demo.store.web.rest;

import com.adyen.demo.store.StoreApp;
import com.adyen.demo.store.domain.PaymentCache;
import com.adyen.demo.store.domain.User;
import com.adyen.demo.store.repository.PaymentCacheRepository;
import com.adyen.demo.store.service.PaymentCacheService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PaymentCacheResource} REST controller.
 */
@SpringBootTest(classes = StoreApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PaymentCacheResourceIT {

    private static final String DEFAULT_ORDER_REF = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_REF = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGINAL_HOST = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_HOST = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_TYPE = "BBBBBBBBBB";

    @Autowired
    private PaymentCacheRepository paymentCacheRepository;

    @Autowired
    private PaymentCacheService paymentCacheService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentCacheMockMvc;

    private PaymentCache paymentCache;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentCache createEntity(EntityManager em) {
        PaymentCache paymentCache = new PaymentCache()
            .orderRef(DEFAULT_ORDER_REF)
            .originalHost(DEFAULT_ORIGINAL_HOST)
            .paymentData(DEFAULT_PAYMENT_DATA)
            .paymentType(DEFAULT_PAYMENT_TYPE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        paymentCache.setUser(user);
        return paymentCache;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentCache createUpdatedEntity(EntityManager em) {
        PaymentCache paymentCache = new PaymentCache()
            .orderRef(UPDATED_ORDER_REF)
            .originalHost(UPDATED_ORIGINAL_HOST)
            .paymentData(UPDATED_PAYMENT_DATA)
            .paymentType(UPDATED_PAYMENT_TYPE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        paymentCache.setUser(user);
        return paymentCache;
    }

    @BeforeEach
    public void initTest() {
        paymentCache = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentCache() throws Exception {
        int databaseSizeBeforeCreate = paymentCacheRepository.findAll().size();

        // Create the PaymentCache
        restPaymentCacheMockMvc.perform(post("/api/payment-caches")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentCache)))
            .andExpect(status().isCreated());

        // Validate the PaymentCache in the database
        List<PaymentCache> paymentCacheList = paymentCacheRepository.findAll();
        assertThat(paymentCacheList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentCache testPaymentCache = paymentCacheList.get(paymentCacheList.size() - 1);
        assertThat(testPaymentCache.getOrderRef()).isEqualTo(DEFAULT_ORDER_REF);
        assertThat(testPaymentCache.getOriginalHost()).isEqualTo(DEFAULT_ORIGINAL_HOST);
        assertThat(testPaymentCache.getPaymentData()).isEqualTo(DEFAULT_PAYMENT_DATA);
        assertThat(testPaymentCache.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    public void createPaymentCacheWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentCacheRepository.findAll().size();

        // Create the PaymentCache with an existing ID
        paymentCache.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentCacheMockMvc.perform(post("/api/payment-caches")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentCache)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentCache in the database
        List<PaymentCache> paymentCacheList = paymentCacheRepository.findAll();
        assertThat(paymentCacheList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkOrderRefIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentCacheRepository.findAll().size();
        // set the field null
        paymentCache.setOrderRef(null);

        // Create the PaymentCache, which fails.

        restPaymentCacheMockMvc.perform(post("/api/payment-caches")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentCache)))
            .andExpect(status().isBadRequest());

        List<PaymentCache> paymentCacheList = paymentCacheRepository.findAll();
        assertThat(paymentCacheList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPaymentCaches() throws Exception {
        // Initialize the database
        paymentCacheRepository.saveAndFlush(paymentCache);

        // Get all the paymentCacheList
        restPaymentCacheMockMvc.perform(get("/api/payment-caches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentCache.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderRef").value(hasItem(DEFAULT_ORDER_REF)))
            .andExpect(jsonPath("$.[*].originalHost").value(hasItem(DEFAULT_ORIGINAL_HOST)))
            .andExpect(jsonPath("$.[*].paymentData").value(hasItem(DEFAULT_PAYMENT_DATA)))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE)));
    }
    
    @Test
    @Transactional
    public void getPaymentCache() throws Exception {
        // Initialize the database
        paymentCacheRepository.saveAndFlush(paymentCache);

        // Get the paymentCache
        restPaymentCacheMockMvc.perform(get("/api/payment-caches/{id}", paymentCache.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentCache.getId().intValue()))
            .andExpect(jsonPath("$.orderRef").value(DEFAULT_ORDER_REF))
            .andExpect(jsonPath("$.originalHost").value(DEFAULT_ORIGINAL_HOST))
            .andExpect(jsonPath("$.paymentData").value(DEFAULT_PAYMENT_DATA))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE));
    }

    @Test
    @Transactional
    public void getNonExistingPaymentCache() throws Exception {
        // Get the paymentCache
        restPaymentCacheMockMvc.perform(get("/api/payment-caches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentCache() throws Exception {
        // Initialize the database
        paymentCacheService.save(paymentCache);

        int databaseSizeBeforeUpdate = paymentCacheRepository.findAll().size();

        // Update the paymentCache
        PaymentCache updatedPaymentCache = paymentCacheRepository.findById(paymentCache.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentCache are not directly saved in db
        em.detach(updatedPaymentCache);
        updatedPaymentCache
            .orderRef(UPDATED_ORDER_REF)
            .originalHost(UPDATED_ORIGINAL_HOST)
            .paymentData(UPDATED_PAYMENT_DATA)
            .paymentType(UPDATED_PAYMENT_TYPE);

        restPaymentCacheMockMvc.perform(put("/api/payment-caches")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPaymentCache)))
            .andExpect(status().isOk());

        // Validate the PaymentCache in the database
        List<PaymentCache> paymentCacheList = paymentCacheRepository.findAll();
        assertThat(paymentCacheList).hasSize(databaseSizeBeforeUpdate);
        PaymentCache testPaymentCache = paymentCacheList.get(paymentCacheList.size() - 1);
        assertThat(testPaymentCache.getOrderRef()).isEqualTo(UPDATED_ORDER_REF);
        assertThat(testPaymentCache.getOriginalHost()).isEqualTo(UPDATED_ORIGINAL_HOST);
        assertThat(testPaymentCache.getPaymentData()).isEqualTo(UPDATED_PAYMENT_DATA);
        assertThat(testPaymentCache.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentCache() throws Exception {
        int databaseSizeBeforeUpdate = paymentCacheRepository.findAll().size();

        // Create the PaymentCache

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentCacheMockMvc.perform(put("/api/payment-caches")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentCache)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentCache in the database
        List<PaymentCache> paymentCacheList = paymentCacheRepository.findAll();
        assertThat(paymentCacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePaymentCache() throws Exception {
        // Initialize the database
        paymentCacheService.save(paymentCache);

        int databaseSizeBeforeDelete = paymentCacheRepository.findAll().size();

        // Delete the paymentCache
        restPaymentCacheMockMvc.perform(delete("/api/payment-caches/{id}", paymentCache.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentCache> paymentCacheList = paymentCacheRepository.findAll();
        assertThat(paymentCacheList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
