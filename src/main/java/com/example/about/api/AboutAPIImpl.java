package com.example.about.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 * REST implementation of the AboutAPI interface. There may be other implementations,
 * e.g., a direct Lucene query which might be faster than going through the REST service
 *
 * Created by Kerrie on 1/5/2016.
 */
public class AboutAPIImpl {

    @Autowired
    private RestTemplate restTemplate;

}
