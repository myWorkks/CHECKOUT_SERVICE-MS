package com.bharath.checkout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CartCheckoutMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartCheckoutMsApplication.class, args);
	}

}
