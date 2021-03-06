package com.example;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * Created by Dominik on 23.04.2017.
 */
@Configuration
public class DataBaseSourceConfig {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    @Profile("postgres")
    public DataSource postgresDataSource(){
        return DataSourceBuilder.create().build();
    }
}
