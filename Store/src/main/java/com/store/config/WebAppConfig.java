package com.store.config;

import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
@ComponentScan("com.store.*")
@EnableWebMvc
public class WebAppConfig extends WebMvcConfigurerAdapter {
	
	@Bean
	public UrlBasedViewResolver setupViewResolver() {
		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
		resolver.setPrefix( "/WEB-INF/views/" );
		resolver.setSuffix(".jsp");
		resolver.setViewClass( JstlView.class );
		return resolver;
	}
	
	@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
        super.configureMessageConverters(converters);
    }
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
	
	/*@Bean(name = "multipartResolver")
    public CommonsMultipartResolver getMultipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxUploadSize(1000000);
        return resolver;
    }*/
	
	/*@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource bundle = new ReloadableResourceBundleMessageSource();
		bundle.setBasename("classpath: messages");
		bundle.setDefaultEncoding("UTF-8");
		return bundle;
	}*/
	
	@Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages");
        source.setDefaultEncoding( "UTF-8" );
        return source;
    }
	
	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver locale = new CookieLocaleResolver();
		locale.setDefaultLocale(Locale.ENGLISH);
		locale.setCookieName("myAppLocaleCookie");
		locale.setCookieMaxAge(3600);
		return locale;
	}
	
	@Bean 
	public LocaleChangeInterceptor localeChangeInterceptor(){
	    LocaleChangeInterceptor localeChangeInterceptor=new LocaleChangeInterceptor();
	    localeChangeInterceptor.setParamName("locale");
	    return localeChangeInterceptor;
	}
	
	public void addInterceptors(InterceptorRegistry registry) {
		   registry.addInterceptor(localeChangeInterceptor());
		}
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
}
