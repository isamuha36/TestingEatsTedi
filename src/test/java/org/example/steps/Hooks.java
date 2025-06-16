package org.example.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks extends BaseTest {

    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("Starting scenario: " + scenario.getName());
        try {
            setUp();
        } catch (Exception e) {
            // Memberikan pesan error yang jelas jika setup gagal
            throw new RuntimeException("Failed to setup driver: " + e.getMessage(), e);
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        System.out.println("Finished scenario: " + scenario.getName() + " with status: " + scenario.getStatus());
        tearDown();
    }
}