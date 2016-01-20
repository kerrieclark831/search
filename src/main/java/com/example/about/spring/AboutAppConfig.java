package com.example.about.spring;

import com.example.about.api.AboutAPIImpl;
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
public class AboutAppConfig extends WebMvcConfigurerAdapter {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    AboutAPIImpl api() {
        return new AboutAPIImpl();
    }

}