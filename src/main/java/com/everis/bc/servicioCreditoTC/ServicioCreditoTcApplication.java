package com.everis.bc.servicioCreditoTC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@EnableEurekaClient
@SpringBootApplication
public class ServicioCreditoTcApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioCreditoTcApplication.class, args);
	}

}
