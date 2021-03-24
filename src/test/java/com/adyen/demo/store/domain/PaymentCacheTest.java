package com.adyen.demo.store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.adyen.demo.store.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentCacheTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentCache.class);
        PaymentCache paymentCache1 = new PaymentCache();
        paymentCache1.setId(1L);
        PaymentCache paymentCache2 = new PaymentCache();
        paymentCache2.setId(paymentCache1.getId());
        assertThat(paymentCache1).isEqualTo(paymentCache2);
        paymentCache2.setId(2L);
        assertThat(paymentCache1).isNotEqualTo(paymentCache2);
        paymentCache1.setId(null);
        assertThat(paymentCache1).isNotEqualTo(paymentCache2);
    }
}
