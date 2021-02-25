package com.co.evertec.dba;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "masterAEntityManagerFactory",
        transactionManagerRef = "masterATransactionManager",
        basePackages = {"com.co.evertec.entity"})
public class MasterARepositoryConfig {
	
	@Autowired
	@Qualifier("MasterADataSource")
    private DataSource masterADataSource;
	
	@Autowired
    JpaVendorAdapter jpaVendorAdapter;

    @Value("${mastera.datasource.hibernate.dialect}")
    private String dialect;
    
    public MasterARepositoryConfig() {
		super();
	}

    @Bean(name = "masterAEntityManager")
    public EntityManager entityManager() {
        return masterAEntityManagerFactory().createEntityManager();
    }

    @PersistenceContext(unitName = "masterAEntityManagerFactory")
    @Bean(name = "masterAEntityManagerFactory")
    public EntityManagerFactory masterAEntityManagerFactory() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", dialect);

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(masterADataSource);
        emf.setJpaVendorAdapter(jpaVendorAdapter);
        emf.setPackagesToScan("com.co.evertec.entity");   // <- package for entities
        emf.setPersistenceUnitName("masterAEntityManager");
        emf.setJpaProperties(properties);
        emf.afterPropertiesSet();
        return emf.getObject();
    }

    @Bean(name = "masterATransactionManager")
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(masterAEntityManagerFactory());
    }
}