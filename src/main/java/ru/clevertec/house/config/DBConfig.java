package ru.clevertec.house.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
@RequiredArgsConstructor
@ComponentScan("ru.clevertec")
@PropertySource("classpath:application.yml")
public class DBConfig {

    private static final String USER_KEY = "user";
    private static final String DRIVER_KEY = "driver";
    private static final String URL_KEY = "url";
    private static final String PASSWORD_KEY = "password";
    private static final String CACHE_PREP_STMTS = "cachePrepStmts";
    private static final String PREP_STMT_CACHE_SIZE = "prepStmtCacheSize";
    private static final String PREP_STMT_CACHE_SQL_LIMIT = "prepStmtCacheSqlLimit";
    private static final String CHANGELOG_FILE = "db/changelog.xml";

    private final Environment environment;

    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(environment.getProperty(URL_KEY));
        config.setUsername(environment.getProperty(USER_KEY));
        config.setPassword(environment.getProperty(PASSWORD_KEY));
        config.setDriverClassName(environment.getProperty(DRIVER_KEY));
        config.addDataSourceProperty(CACHE_PREP_STMTS, environment.getProperty(CACHE_PREP_STMTS));
        config.addDataSourceProperty(PREP_STMT_CACHE_SIZE, environment.getProperty(PREP_STMT_CACHE_SIZE));
        config.addDataSourceProperty(PREP_STMT_CACHE_SQL_LIMIT, environment.getProperty(PREP_STMT_CACHE_SQL_LIMIT));
        return config;
    }

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource(hikariConfig());
    }

    @Bean
    public Connection connection(DataSource dataSource) throws SQLException {
        return dataSource.getConnection();
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(CHANGELOG_FILE);
        liquibase.setDataSource(dataSource);
        return liquibase;
    }
}
