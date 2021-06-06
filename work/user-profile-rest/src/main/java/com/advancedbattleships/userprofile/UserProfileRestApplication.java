package com.advancedbattleships.userprofile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@ComponentScan("com.advancedbattleships")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserProfileRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserProfileRestApplication.class, args);
	}

}
