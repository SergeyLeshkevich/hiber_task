package ru.clevertec.house.config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@Configuration
public class TestConfig {

    private static final String PERSISTENCE_UNIT_NAME = "myPersistenceUnit";
//
//    @Bean
//    public DataSource dataSource() {
//        HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setDriverClassName("org.h2.Driver");
//        hikariConfig.setJdbcUrl("jdbc:h2:mem:test");
//        hikariConfig.setUsername("test");
//        hikariConfig.setPassword("test");
//
//        return new HikariDataSource(hikariConfig);
//    }
//
//    @Bean
//    public EntityManagerFactory entityManagerFactory(){
//        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
//    }


}
