package ru.clevertec.house.config;

import com.google.gson.Gson;
import jakarta.persistence.Persistence;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.clevertec.house.util.DateManager;

import javax.sql.DataSource;

@Configuration
@ComponentScan("ru.clevertec")
@RequiredArgsConstructor
public class AppConfig {

    private static final String PERSISTENCE_UNIT_NAME = "PersistenceUnit";

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public Gson getGson(){
        return DateManager.buildGson();
    }
}

