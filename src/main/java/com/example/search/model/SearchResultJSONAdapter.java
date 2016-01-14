package com.example.search.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An adapter that adapts the JSON returned from a backend search call to
 * talk on the SearchResult domain object interface used in the controller.
 *
 * Created by Kerrie on 1/5/2016.
 */
public class SearchResultJSONAdapter implements SearchResult {

    private JsonNode json;

    public SearchResultJSONAdapter(JsonNode json) {
        this.json = json;
    }

    public int getTotalItems() {
        return json.get("count").asInt();
    }

    public List<SearchResultItem> getItems() {
        List<SearchResultItem> items = new ArrayList<>();
        Iterator<JsonNode> it = json.get("searchItems").elements();
        while (it.hasNext()) {
            items.add(new SearchResultItemJSONAdapter(it.next()));
        }
        return items;
    }
}
