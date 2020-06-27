package com.example.minikube.demo.kubedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;



@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, 
		SecurityAutoConfiguration.class,
		ManagementWebSecurityAutoConfiguration.class})
public class KubeDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(KubeDemoApplication.class, args);
	}

}
