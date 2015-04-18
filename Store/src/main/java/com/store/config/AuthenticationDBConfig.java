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
@EnableJpaRepositories( basePackages = "com.store.jpa.auth.repository", entityManagerFactoryRef = "authEntityManager", transactionManagerRef = "authTransactionManager" )
public class AuthenticationDBConfig {
	
	public AuthenticationDBConfig() {
		super();
	}
	
    @Autowired
    private Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean authEntityManager() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource( emasDataSource() );
        em.setPackagesToScan(new String[] { "com.store.jpa.auth.entity" });

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter( vendorAdapter );
		em.setJpaProperties( additionalProperties() );

        return em;
    }

    @Bean
    public DataSource emasDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName( env.getProperty( "db.driver" ) );
        dataSource.setUrl( env.getProperty( "db.url.auth" ) );
        dataSource.setUsername( env.getProperty( "db.username" ) );
        dataSource.setPassword( env.getProperty( "db.password" ) );

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager authTransactionManasger() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory( authEntityManager().getObject() );
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