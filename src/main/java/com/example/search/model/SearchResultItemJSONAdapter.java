package com.example.search.model;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * An adapter that adapts the JSON returned from a backend search call to
 * talk on the SearchResultItem domain object interface used in the controller.
 *
 * Created by Kerrie on 1/5/2016.
 */
public class SearchResultItemJSONAdapter implements SearchResultItem {

    private JsonNode json;

    public SearchResultItemJSONAdapter(JsonNode json) {
        this.json = json;
    }

    public String getCaption() {
        return json.get("caption").asText();
    }

    public String getDescription() {
        return json.get("the_Description").asText();
    }

    public String getCatalogName() { return json.get("catalogName").asText(); }

    public String getImageURL() {
        return json.get("imageURI").asText();
    }

    public String getPrice() {
        return json.get("priceInfo").get("price").asText();
    }

}
