package com.example.search.model;

/**
 * Domain object interface representing a single item returned from a search query
 *
 * Created by Kerrie on 1/5/2016.
 */
public interface SearchResultItem {

    /**
     * Get the item's caption (title), e.g., "Ugly Red Sweater"
     * @return The caption
     */
    public String getCaption();

    /**
     * Get the item's description, e.g., the description associated with a product
     *
     * @return The description
     */
    public String getDescription();

    /**
     * Get the name of the catalog (store) offering the item
     * @return The name of the catalog
     */
    public String getCatalogName();

    /**
     * Get the URL of the item's thumbnail image
     *
     * @return The thumbnail image URL
     */
    public String getImageURL();

    /**
     * Get the item's formatted price (already includes currency symbol)
     *
     * @return The formatted price
     */
    public String getPrice();

}
