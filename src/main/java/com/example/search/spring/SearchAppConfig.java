package com.example.search.spring;

import com.example.search.api.SearchAPIImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Spring Java application configuration - no XML hell!
 *
 * Created by Kerrie on 1/5/2016.
 */
@Configuration
public class SearchAppConfig extends WebMvcConfigurerAdapter {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    SearchAPIImpl api() {
        return new SearchAPIImpl();
    }

}