package com.example.area_batch.app.data_source;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.area_batch.app.repo",
                 entityManagerFactoryRef = "targetEntityManagerFactory", 
                transactionManagerRef = "targetTransactionManager")
public class TargetDataSourceConfig {
    @Value("${spring.datasource.target.url}")
    private String targetDbUrl;
    @Value("${spring.datasource.target.username}")
    private String targetDbUserName;
    @Value("${spring.datasource.target.password}")
    private String targetDbPassword;
    @Value("${spring.datasource.target.driver-class-name}")
    private String targetDbDriverClassName;

    
    @Primary
    @Bean(name = "targetDataSource")
    public DataSource dataSource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(targetDbUrl);
        dataSource.setUsername(targetDbUserName);
        dataSource.setPassword(targetDbPassword);
        dataSource.setDriverClassName(targetDbDriverClassName);
        return dataSource;
    }

    @Primary
    @Bean(name =  "targetEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean localEntityManagerFactory(EntityManagerFactoryBuilder builder
        , @Qualifier("targetDataSource")DataSource dataSource){
            LocalContainerEntityManagerFactoryBean entityManagerFactory = builder
                .dataSource(dataSource)
                .packages("com.example.area_batch.app.model.entity.target")
                .persistenceUnit("targetEntityManagerFactory")
                .build();
            return entityManagerFactory;

        }

    @Primary
    @Bean(name = "targetTransactionManager")
    public PlatformTransactionManager localTransactionManager(
            @Qualifier("targetEntityManagerFactory") EntityManagerFactory entityManagerFactory ){
                return new JpaTransactionManager(entityManagerFactory);
    }
}
