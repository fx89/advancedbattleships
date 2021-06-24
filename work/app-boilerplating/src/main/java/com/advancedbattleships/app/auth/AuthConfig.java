package com.advancedbattleships.app.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@ConfigurationProperties("advancedbattleships.auth")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthConfig {
	private String authSuccessRedirectUrl;
}
