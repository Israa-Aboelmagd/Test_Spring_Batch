package com.example.area_batch.app.data_source;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
// @EnableJpaRepositories(basePackages = "com.example.batch")
// @EnableJpaRepositories(basePackages = "com.example.area_batch.app.repo",
//                  entityManagerFactoryRef = "sourceEntityManagerFactory", 
//                 transactionManagerRef = "sourceTransactionManager")

public class MainDataSourceConfig {
    @Value("${spring.datasource.source.url}")
    private String sourceDbUrl;
    @Value("${spring.datasource.source.username}")
    private String sourceDbUserName;
    @Value("${spring.datasource.source.password}")
    private String sourceDbPassword;
    @Value("${spring.datasource.source.driver-class-name}")
    private String sourceDbDriverClassName;
   
    @Bean(name = "sourceDataSource")
    public DataSource dataSource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(sourceDbUrl);
        dataSource.setUsername(sourceDbUserName);
        dataSource.setPassword(sourceDbPassword);
        dataSource.setDriverClassName(sourceDbDriverClassName);
        return dataSource;
    }

    @Bean(name =  "sourceEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean externalEntityManagerFactory(EntityManagerFactoryBuilder builder
        , @Qualifier("sourceDataSource")DataSource dataSource){
            LocalContainerEntityManagerFactoryBean entityManagerFactory = builder
                .dataSource(dataSource)
                .packages("com.example.area_batch.app.model.entity.source")
                .persistenceUnit("sourceEntityManagerFactory")
                .build();
            return entityManagerFactory;

        }

    @Bean(name = "sourceTransactionManager")
    public PlatformTransactionManager externalTransactionManager(
            @Qualifier("sourceEntityManagerFactory") EntityManagerFactory entityManagerFactory ){
                return new JpaTransactionManager(entityManagerFactory);
    }
    
    
}



