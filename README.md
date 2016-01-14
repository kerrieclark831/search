# Application Bulkheads at SHOP.COM

The eCommerce platform on which we work (http://www.shop.com) is undergoing a major overhaul from an aging monolithic application to a more distributed SOA architecture. Part of this effort involves breaking out functional pieces of the front end application into separately managed and deployed web applications that together make up the SHOP.COM website. For example, we have applications for:

* Search
* Product Catalog
* Shopping Cart
* Checkout
* etc.

This is known as a bulkhead pattern. Analagous to a bulkhead on a ship, its main purpose is to isolate failure to smaller areas of the system so if one piece fails the entire system doesn't go down. Decomposing the application has additional benefits - a smaller codebase for each application is easier to grok and is more manageable. Developers have less chance of stepping on each others' toes, and teams can form around the individual applications. Applications can be deployed independently. However, it also incurs some complexity in deployments and monitoring.

## About This Example

At SHOP.COM, each front end bulkhead is a Spring MVC application initialized with Spring Boot, running with an embedded Tomcat server. It literally takes mere minutes to get an application up and running. This example is a toy search application intended to demonstrate the code patterns that each front end application should adhere to. It submits, processes and displays a real search request, but the UI is not what you would see on SHOP.COM. It is intentionally minimal and low on style. The goal here is to provide a blueprint for writing code to retrieve data from the back end and prepare it for display. The process is the same for each application.

If you'd like to see what a real search looks like on SHOP.COM, try this: [Show me ugly sweaters!](http://www.shop.com/search/ugly+sweater)

## How it Works

Servicing an application request proceeds through several code layers, each of which performs a specific function:

### The Controller

An incoming request enters the application through a Spring MVC controller. The sole responsibility of the controller method is to retrieve data from the back end and render the view with the data. There should be no business logic in the controller! In our example, a controller method looks like this:

```
    @RequestMapping(path = "/results", method = RequestMethod.GET)
    public String getResults(@RequestParam String term,
                             ModelMap modelMap) {
        SearchResult result = api.executeSearch(term);
        modelMap.put("term", term);
        modelMap.put("searchResult", result);
        return "results";
    }
```

**This is not an over-simplication.** All controller methods should be this easy - retrieve and format data via an API call, populate the view, and render. Done.

### The API

Each front end application contains an API (Java interface and implementation(s)) that defines the Domain Specific Language (DSL) for that application. The API method names are as expressive and fluent as possible in the context of the feature. In our small exmaple, all we need to do is execute a search:

```
public interface SearchAPI {

    /**
     * Execute a search query with the given term
     *
     * @param term The term to search for
     * @return The search result object nicely formatted for display
     */
    public SearchResult executeSearch(String term);

}
```

The purpose of the API layer is also very simple. It puts together REST URLs to get data from the back end, and returns a domain object that is highly optimized and formatted for display, and returns it to the controller to pass along to the view:

```
    public SearchResult executeSearch(String term) {

        // Build service URL - sorry, this isn't a real URL
        String serviceURL = UriComponentsBuilder.newInstance()
                .host("example.com")
                .port(8089)
                .scheme("http")
                .path("/Search/" + term)
                .toUriString();

        // Call the service
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(serviceURL, JsonNode.class);

        // Adapt the raw data and return to the controller
        return new SearchResultJSONAdapter(response.getBody());
    }
```

That's it! There is a bit of magic going on in that JSONAdapter, which we'll explore in the next section.

It's important to understand the difference between this API layer and the back end REST APIs. In a perfect world the back end APIs would return data exactly how it will be used. Unfortunately our world is not perfect. Quite often multiple REST calls are made and munged into a domain object used by the controller. In other words, the API layer performs **client-specific service composition**.

A common pattern of service composition is to call a REST service that returns a small amount of data along with HATEOAS links to find other related information. For example, a shopper service returns a shopper's email, name, and a few other things. The payload includes other REST URLs to locate the shopper's address book, wallet, orders, etc. The application may opt to use a library such as RxJava to set up an observable, and fire off the HATEOAS calls asynchronously. I will talk more about reducing the chattiness of our APIs in another forum.

**Note the API layer currently resides in application code, but it is coded to an interface so it can easily be split off into a separate process and exposed with a REST or some other API if we wish to move to a highly distributed system. In that case it might be moved to an API Gateway server that orchestrates and composes REST calls for client-specific endpoints.**

### The Magic Adapters

The moment you've been waiting for! How the heck did we get the data returned from the back end search service into a manageable and meaningful object to populate the UI? I can say for a fact that the search payload is heavyweight, but our toy UI only needs an item's thumbnail image, caption, catalog name and price. We don't want the front end developers to worry about sifting through heaps of JSON to fish out the items to display on a page - the templates would be super-messy with stringy logic. the [Adapter Design Pattern](https://en.wikipedia.org/wiki/Adapter_pattern) to the rescue!

In the API implementation mentioned above, we make a call like this when the REST service call returns:

```
    // Adapt the raw data and return to the controller
    return new SearchResultJSONAdapter(response.getBody());
```

The SearchResultJSONAdapter class is an implementation of an interface that uses language appropriate to the search application, and requires little to no logic in the front end templates for display. The interface looks like this:

```
    public interface SearchResult {
    
        /**
         * Get the total number of items in the search result set
         *
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
```

The JSON adapter we're using accepts the JSON payload from the back end service call, and "talks" on the SearchResult interface. It is this adapter that is doing the sifting through the gobs of JSON, allowing the front end (which is implemented with Velocity templates - a Java technology) to utilize strong Java types instead of JSON strings. It ends up looking like this:

```
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
```

Observe that the implementation of each method finds the desired data in the JSON and converts it into strong Java types.

We mentioned previously that our toy UI only needs a few of the 30+ fields returned for each search result item in the JSON payload. There is a similar SearchResultItemJSONAdapter that only exposes what is needed:

```
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
```

While the SHOP.COM REST services generally produce JSON, we could implement adapters for other types of payloads - XML, protocol buffers, etc. Again, coding to the SearchResult and SearchResultItem interfaces saves our butts from having to modify a bunch of code to use a different content type.

### The User Interface

So what impact does all this have on the UI? The SHOP.COM website is very concerned with SEO, and must provide server-generated pages in most cases. We use Apache Velocity templates, which provide simple access to Java objects and a bit of logic in the UI.

In our example, once we execute a search we only want to display the image, caption, catalog name and price. The SearchResultItem interface succinctly exposes only the fields we want. Now in our template, populated with the adapted data, the markup is as simple as this:

```
    <div>
        <img src="$item.getImageURL()">
        <p>$item.getCaption()</p>
        <p>from: $item.getCatalogName()</p>
        <p>$item.getPrice()</p>
        <input type="button" value="Add to Cart">
    </div>
```

If we didn't do the adaptation, our markup could get messy dealing directly with JSON:

```
<div>
    <img src="$json.imageURI.asText()">
    <p>$json.caption.asText()">
    <p>from: $json.catalogName.asText()">
    <p>$json.priceInfo.price.asText()">
</div>
```

This is just a simple example. You can probably imagine how this will turn out when the payload contains lists, maps, and other complex data structures that must be accessed as JSON!

### Don't Forget Your Tests!

Unit testing is especially important in distributed systems - automation is key in unit testing, continuous integration, and continuous deployment. Just to show how it works, a simple JUnit test looks like this:

```
	@Test
	public void executeSearch() {
		String term = "ugly sweater";
		SearchResult result = api.executeSearch(term);
		assert(result.getItems().size() == 12);
	}
```

I skimped here, but you can't. :) You should strive for maximum coverage.

### Wrapping it up

Just in case you're wondering, here are some screenshots of the application running on my laptop. Sorry you can't run the code yourself because the REST service is only available internally. First there is a page with a search bar:

![Search Bar](https://github.com/kerrieclark831/search/blob/master/src/main/resources/static/images/search-bar.PNG)

and here are the search results for "ugly sweater":

![Search Results for "ugly sweater"](https://github.com/kerrieclark831/search/blob/master/src/main/resources/static/images/search-results.PNG)
