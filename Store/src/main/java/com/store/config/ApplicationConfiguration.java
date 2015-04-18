package com.store.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@ComponentScan({"com.store.jpa.store*","com.store.jpa.auth*" })
@Import({ WebAppConfig.class, StoreDBConfig.class, AuthenticationDBConfig.class })
public class ApplicationConfiguration 
{
	
}
