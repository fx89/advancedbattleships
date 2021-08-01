package com.advancedbattleships.social.dataservice.impl.springdata;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
		basePackages 			= "com.advancedbattleships.social.dataservice.impl.springdata.dao",
		entityManagerFactoryRef	= "absSocialEntityManagerFactory",
		transactionManagerRef	= "absSocialTransactionManager"
	)
public class SocialDataSourceConfig {
	@Autowired
	Environment env;

	@Bean
	public LocalContainerEntityManagerFactoryBean absSocialEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(absSocialDataSource());
		em.setPackagesToScan("com.advancedbattleships.social.dataservice.impl.springdata.model");

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.datasource.social.ddl-auto"));
		properties.put("hibernate.dialect", env.getProperty("spring.datasource.social.dialect"));
		properties.put("hibernate.show_sql", env.getProperty("spring.datasource.social.show-sql"));
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Bean
	public DataSource absSocialDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.social.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.datasource.social.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.social.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.social.password"));
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager absSocialTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(absSocialEntityManagerFactory().getObject());
		return transactionManager;
	}
}
