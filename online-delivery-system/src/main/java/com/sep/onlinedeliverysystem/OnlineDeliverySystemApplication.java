package com.sep.onlinedeliverysystem;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;

@SpringBootApplication
@Log
public class OnlineDeliverySystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineDeliverySystemApplication.class, args);
	}

}
