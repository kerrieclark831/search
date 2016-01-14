package com.example.search.api;

import com.example.search.model.SearchResult;

/**
 * Application interface for the search application. This describes a Domain Specific Language (DSL)
 * that is used in the controller and front end template views.
 *
 * Created by Kerrie on 1/5/2016.
 */
public interface SearchAPI {

    /**
     * Execute a search query with the given term
     * @param term The term to search for
     * @return The search result object nicely formatted for display
     */
    public SearchResult executeSearch(String term);

}
