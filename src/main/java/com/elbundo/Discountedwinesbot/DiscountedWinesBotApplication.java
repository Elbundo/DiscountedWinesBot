package com.elbundo.Discountedwinesbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiscountedWinesBotApplication {
	public static final double MIN_PRICE = 500;

	public static void main(String[] args) {
		SpringApplication.run(DiscountedWinesBotApplication.class, args);
	}

}
