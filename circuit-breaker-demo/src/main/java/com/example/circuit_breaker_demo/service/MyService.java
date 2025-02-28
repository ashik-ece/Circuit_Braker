package com.example.circuit_breaker_demo.service;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class MyService {

    public String someMethod() {
        Random random = new Random();
        int value = random.nextInt(10);

        if (value < 3) {
            throw new RuntimeException("Simulated service failure!");
        }

        return "Service response is successful!";
    }

}
