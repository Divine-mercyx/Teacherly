package org.Teacherly.services.servicesImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SubscriptionServiceImplTest {

    @Autowired
    private SubscriptionServiceImpl subscriptionService;

    @BeforeEach
    void setUp() {
        subscriptionService.deleteAll();
    }

    @AfterEach
    void tearDown() {
        subscriptionService.deleteAll();
    }
}