package com.store.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource({ "classpath:database.properties" })
@EnableTransactionManagement
@EnableJpaRepositories( basePackages = "com.store.jpa.store.repository", entityManagerFactoryRef = "storeEntityManager", transactionManagerRef = "storeTransactionManager" )
public class StoreDBConfig {
	
	public StoreDBConfig() {
		super();
	}
	
    @Autowired
    private Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean storeEntityManager() {
    	
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource( testDataSource() );
        em.setPackagesToScan(new String[] { "com.testing.jpa.store.entity" });

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter( vendorAdapter );
		em.setJpaProperties( additionalProperties() );

        return em;
    }

    @Bean
    public DataSource testDataSource() {
    	
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName( env.getProperty( "db.driver" ) );
        dataSource.setUrl( env.getProperty( "db.url.store" ) );
        dataSource.setUsername( env.getProperty( "db.username" ) );
        dataSource.setPassword( env.getProperty( "db.password" ) );

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager storeTransactionManager() {
    	
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory( storeEntityManager().getObject() );
        transactionManager.setRollbackOnCommitFailure( true );
        return transactionManager;
    }
    
    final Properties additionalProperties() {
		
		final Properties hibernateProperties = new Properties();
		
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty( "hibernate.hbm2ddl.auto" ) );
        hibernateProperties.setProperty( "hibernate.show_sql", env.getProperty( "hibernate.show_sql" ) );
        hibernateProperties.setProperty( "hibernate.dialect", env.getProperty( "hibernate.dialect" ) );
		return hibernateProperties;
	}

}