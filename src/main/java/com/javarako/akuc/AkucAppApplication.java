package com.javarako.akuc;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AkucAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AkucAppApplication.class, args);
	}

    @PostConstruct
    public void init(){
      // Setting Spring Boot SetTimeZone
      TimeZone.setDefault(TimeZone.getTimeZone("EST"));
    }
}
