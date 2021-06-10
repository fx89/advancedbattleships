package com.advancedbattleships.security.dataservice.impl.springdata;

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
	basePackages 			= "com.advancedbattleships.security.dataservice.impl.springdata.dao",
	entityManagerFactoryRef	= "absSecurityEntityManagerFactory",
	transactionManagerRef	= "absSecurityTransactionManager"
)
public class SecurityDataSourceConfig {
	@Autowired
	Environment env;

	@Bean
	public LocalContainerEntityManagerFactoryBean absSecurityEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(absSecurityDataSource());
		em.setPackagesToScan("com.advancedbattleships.security.dataservice.impl.springdata.model");

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.datasource.security.ddl-auto"));
		properties.put("hibernate.dialect", env.getProperty("spring.datasource.security.dialect"));
		properties.put("hibernate.show_sql", env.getProperty("spring.datasource.security.show-sql"));
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Bean
	public DataSource absSecurityDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.security.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.datasource.security.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.security.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.security.password"));
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager absSecurityTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(absSecurityEntityManagerFactory().getObject());
		return transactionManager;
	}
}
