package com.advancedbattleships.content.dataservice.impl.springdata;

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
		basePackages 			= "com.advancedbattleships.content.dataservice.impl.springdata.dao",
		entityManagerFactoryRef	= "absContentEntityManagerFactory",
		transactionManagerRef	= "absContentTransactionManager"
	)
public class ContentDataSourceConfig {
	@Autowired
	Environment env;

	@Bean
	public LocalContainerEntityManagerFactoryBean absContentEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(absContentDataSource());
		em.setPackagesToScan("com.advancedbattleships.content.dataservice.impl.springdata.model");

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.datasource.content.ddl-auto"));
		properties.put("hibernate.dialect", env.getProperty("spring.datasource.content.dialect"));
		properties.put("hibernate.show_sql", env.getProperty("spring.datasource.content.show-sql"));
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Bean
	public DataSource absContentDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.content.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.datasource.content.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.content.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.content.password"));
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager absContentTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(absContentEntityManagerFactory().getObject());
		return transactionManager;
	}
}
