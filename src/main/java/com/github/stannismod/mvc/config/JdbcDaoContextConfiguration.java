package com.github.stannismod.mvc.config;

import com.github.stannismod.mvc.dao.JdbcTaskDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * @author akirakozov
 */
@Configuration
public class JdbcDaoContextConfiguration {
    @Bean
    public JdbcTaskDao productJdbcDao(DataSource dataSource) {
        return new JdbcTaskDao(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:product.db");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }
}
