package com.example.TransportCompany;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.TransportCompany.repository")
@EntityScan("com.example.TransportCompany.model")
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class TransportCompanyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransportCompanyApplication.class, args);
	}

}
