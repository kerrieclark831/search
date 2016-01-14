package com.example.search.spring;

import com.example.search.api.SearchAPIImpl;
import com.example.search.model.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * Spring MVC controller for toy search application that demonstrates
 * the code structure for SHOP.COM bulkhead applications.
 *
 * Created by Kerrie on 1/3/2016.
 */
@Controller
public class SearchController {

    @Autowired
    private SearchAPIImpl api;

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public String get(ModelMap modelMap) {
        SearchForm searchForm = new SearchForm();
        modelMap.addAttribute("searchForm", searchForm);
        return "search";
    }

    @RequestMapping(path = "/results", method = RequestMethod.POST)
    public String executeSearch(@ModelAttribute("searchForm") SearchForm searchForm,
                                ModelMap modelMap) {
        System.out.println("Searching for: " + searchForm.getTerm());
        return "redirect:/results?term=" + searchForm.getTerm();
    }

    @RequestMapping(path = "/results", method = RequestMethod.GET)
    public String getResults(@RequestParam String term,
                             ModelMap modelMap) {
        SearchResult result = api.executeSearch(term);
        modelMap.put("term", term);
        modelMap.put("searchResult", result);
        return "results";
    }

}
