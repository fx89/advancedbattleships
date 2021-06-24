package com.advancedbattleships.content.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@ConfigurationProperties("advancedbattleships.content")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContentServiceConfig {
	private String defaultWallpaperName;

	private String defaultStylesheetName;

	private String defaultIconThemeName;

	private String defaultLogoName;
}
