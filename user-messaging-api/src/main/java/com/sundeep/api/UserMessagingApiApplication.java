package com.sundeep.api;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@SpringBootApplication

public class UserMessagingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserMessagingApiApplication.class, args);
	}

	@Bean
	NamedParameterJdbcTemplate namedParameterJdbcTemplate( final DataSource dataSource)
	{
		return new NamedParameterJdbcTemplate( dataSource );
	}

	@Bean
	JdbcTemplate jdbcTemplate (final DataSource dataSource)
	{
		return new JdbcTemplate( dataSource );
	}

}
