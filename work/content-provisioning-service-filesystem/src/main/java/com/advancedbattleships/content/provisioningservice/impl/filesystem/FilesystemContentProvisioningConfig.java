package com.advancedbattleships.content.provisioningservice.impl.filesystem;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@ConfigurationProperties("advancedbattleships.content.filesystem")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FilesystemContentProvisioningConfig {
	private String storageDir;
}
