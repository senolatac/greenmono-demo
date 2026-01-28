package com.greenmono.mealplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MealPlannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MealPlannerApplication.class, args);
    }
}
