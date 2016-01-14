package com.example.search.api;

import com.example.search.model.SearchResult;
import com.example.search.model.SearchResultJSONAdapter;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.yaml.snakeyaml.util.UriEncoder;

import javax.xml.ws.Response;

/**
 * REST implementation of the SearchAPI interface. There may be other implementations,
 * e.g., a direct Lucene query which might be faster than going through the REST service
 *
 * Created by Kerrie on 1/5/2016.
 */
public class SearchAPIImpl {

    @Autowired
    private RestTemplate restTemplate;

    public SearchResult executeSearch(String term) {

        // Build service URL - not the real one!
        String serviceURL = UriComponentsBuilder.newInstance()
                .host("example.com")
                .port(8089)
                .scheme("http")
                .path("/Search/" + term)
                .toUriString();

        ResponseEntity<JsonNode> response = restTemplate.getForEntity(serviceURL, JsonNode.class);

        return new SearchResultJSONAdapter(response.getBody());
    }
}
