package com.example.alunoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AlunoserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlunoserviceApplication.class, args);
	}

}
