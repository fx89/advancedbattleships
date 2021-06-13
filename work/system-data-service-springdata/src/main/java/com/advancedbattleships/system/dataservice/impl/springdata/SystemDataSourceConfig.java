package com.advancedbattleships.system.dataservice.impl.springdata;

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
		basePackages 			= "com.advancedbattleships.system.dataservice.impl.springdata.dao",
		entityManagerFactoryRef	= "absSystemEntityManagerFactory",
		transactionManagerRef	= "absSystemTransactionManager"
	)
public class SystemDataSourceConfig {
	@Autowired
	Environment env;

	@Bean
	public LocalContainerEntityManagerFactoryBean absSystemEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(absSystemDataSource());
		em.setPackagesToScan("com.advancedbattleships.system.dataservice.impl.springdata.model");

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.datasource.system.ddl-auto"));
		properties.put("hibernate.dialect", env.getProperty("spring.datasource.system.dialect"));
		properties.put("hibernate.show_sql", env.getProperty("spring.datasource.system.show-sql"));
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Bean
	public DataSource absSystemDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.system.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.datasource.system.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.system.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.system.password"));
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager absSystemTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(absSystemEntityManagerFactory().getObject());
		return transactionManager;
	}
}
