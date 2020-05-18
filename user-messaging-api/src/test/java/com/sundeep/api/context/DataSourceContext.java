package com.sundeep.api.context;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.sundeep.api.config.datasource"})
public class DataSourceContext {
}
