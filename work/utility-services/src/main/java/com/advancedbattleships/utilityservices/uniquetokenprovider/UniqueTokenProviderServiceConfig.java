package com.advancedbattleships.utilityservices.uniquetokenprovider;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@ConfigurationProperties("advancedbattleships.uniquetokenprovider")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UniqueTokenProviderServiceConfig {
	private String datadir;
	private char instanceId;
}
