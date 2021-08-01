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
		basePackages 			= "com.advancedbattleships.chat.dataservice.impl.springdata.dao",
		entityManagerFactoryRef	= "absChatEntityManagerFactory",
		transactionManagerRef	= "absChatTransactionManager"
	)
public class ChatDataSourceConfig {
	@Autowired
	Environment env;

	@Bean
	public LocalContainerEntityManagerFactoryBean absChatEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(absChatDataSource());
		em.setPackagesToScan("com.advancedbattleships.chat.dataservice.impl.springdata.model");

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.datasource.chat.ddl-auto"));
		properties.put("hibernate.dialect", env.getProperty("spring.datasource.chat.dialect"));
		properties.put("hibernate.show_sql", env.getProperty("spring.datasource.chat.show-sql"));
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Bean
	public DataSource absChatDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.chat.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.datasource.chat.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.chat.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.chat.password"));
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager absChatTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(absChatEntityManagerFactory().getObject());
		return transactionManager;
	}
}
