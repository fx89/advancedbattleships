package com.advancedbattleships.inventory.dataservice.impl.springdata;

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
		basePackages 			= "com.advancedbattleships.inventory.dataservice.impl.springdata.dao",
		entityManagerFactoryRef	= "absInventoryEntityManagerFactory",
		transactionManagerRef	= "absInventoryTransactionManager"
	)
public class InventoryDataSourceConfig {
	@Autowired
	Environment env;

	@Bean
	public LocalContainerEntityManagerFactoryBean absInventoryEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(absInventoryDataSource());
		em.setPackagesToScan("com.advancedbattleships.inventory.dataservice.impl.springdata.model");

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.datasource.inventory.ddl-auto"));
		properties.put("hibernate.dialect", env.getProperty("spring.datasource.inventory.dialect"));
		properties.put("hibernate.show_sql", env.getProperty("spring.datasource.inventory.show-sql"));
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Bean
	public DataSource absInventoryDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.inventory.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.datasource.inventory.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.inventory.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.inventory.password"));
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager absInventoryTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(absInventoryEntityManagerFactory().getObject());
		return transactionManager;
	}
}
