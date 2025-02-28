package com.example.circuit_breaker_demo.controller;

import com.example.circuit_breaker_demo.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class MyController {

    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @Autowired
    private MyService myService;

    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    @Autowired
    public MyController(CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @GetMapping("/test-circuit-breaker")
    public String testCircuitBreaker() {
        logger.info("Received request for /test-circuit-breaker");

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("myCircuitBreaker");

        return circuitBreaker.run(
                myService::someMethod,
                throwable -> {
                    logger.error("Fallback triggered due to: {}", throwable.getMessage(), throwable);
                    return "Fallback: Service is unavailable.";
                });
    }
}
