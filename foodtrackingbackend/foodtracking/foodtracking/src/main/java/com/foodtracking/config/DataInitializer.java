package com.foodtracking.config;

import com.foodtracking.service.FoodOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private FoodOrderService foodOrderService;

    @Override
    public void run(String... args) throws Exception {
        foodOrderService.initializeSampleData();
    }
}