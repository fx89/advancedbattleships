package com.advancedbattleships.messaging.dataservice.impl.springdata;

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
@EnableJpaRepositories(basePackages = "com.advancedbattleships.messaging.dataservice.impl.springdata.dao", entityManagerFactoryRef = "absMessagingEntityManagerFactory", transactionManagerRef = "absMessagingTransactionManager")
public class MessagingDataSourceConfig {
	@Autowired
	Environment env;

	@Bean
	public LocalContainerEntityManagerFactoryBean absMessagingEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(absMessagingDataSource());
		em.setPackagesToScan("com.advancedbattleships.messaging.dataservice.impl.springdata.model");

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.datasource.messaging.ddl-auto"));
		properties.put("hibernate.dialect", env.getProperty("spring.datasource.messaging.dialect"));
		properties.put("hibernate.show_sql", env.getProperty("spring.datasource.messaging.show-sql"));
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Bean
	public DataSource absMessagingDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.messaging.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.datasource.messaging.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.messaging.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.messaging.password"));
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager absMessagingTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(absMessagingEntityManagerFactory().getObject());
		return transactionManager;
	}
}
