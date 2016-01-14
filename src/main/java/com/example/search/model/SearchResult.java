package com.example.search.model;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.List;

/**
 * Domain object interface representing the results of a search query
 *
 * Created by Kerrie on 1/5/2016.
 */
public interface SearchResult {

    /**
     * Get the total number of items in the search result set
     * @return The number of items
     */
    public int getTotalItems();

    /**
     * Get the items on the current page; most likely much smaller than the total items
     *
     * @return The list of items on the current page
     */
    public List<SearchResultItem> getItems();

}
