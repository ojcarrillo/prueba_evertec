package com.co.evertec.dba;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DBConfig {

	@Bean(name = "h2DataSource")
	@Primary
	public DataSource h2DataSource(@Value("${h2.url}") String url, 
									@Value("${h2.username}") String username,
									@Value("${h2.password}") String password, 
									@Value("${h2.driver-class-name}") String driver)
			throws NamingException {
		return DataSourceBuilder.create()
				.url(url)
				.driverClassName(driver)
				.username(username)
				.password(password)
				.build();
	}

	@Bean(name = "MasterADataSource")
	@ConfigurationProperties(prefix  = "mastera")
	public DataSource masterCDataSource(@Value("${mastera.datasource.url}") String url, 
										@Value("${mastera.datasource.username}") String username, 
										@Value("${mastera.datasource.password}") String password,
										@Value("${mastera.datasource.platform}") String platform,
										@Value("${mastera.datasource.driver-class-name}") String driver,
										@Value("${mastera.datasource.maximumPoolSize}") Integer maximumPoolSize) throws NamingException {
		
		return configurarConexionHikari(url, username, password, maximumPoolSize);
	}

	private HikariDataSource configurarConexionHikari(String url, String username, String password, Integer maximumPoolSize) {
		HikariConfig config = new HikariConfig();
		
		config.setJdbcUrl( url );
        config.setUsername( username );
        config.setPassword( password );      
        config.setMaximumPoolSize(maximumPoolSize);
        config.setIdleTimeout(100000L);
		config.setMaxLifetime(330000L);
		config.setLeakDetectionThreshold(100000L);
		config.setConnectionTimeout(100000L);
        config.setConnectionTestQuery("SELECT 1");
		
		return new HikariDataSource( config );
	}	
}
