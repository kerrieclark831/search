package com.example.search;

import com.example.search.api.SearchAPIImpl;
import com.example.search.model.SearchResult;
import com.example.search.spring.SearchApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SearchApplication.class)
@WebAppConfiguration
public class SearchApplicationTests {

	@Autowired
	private SearchAPIImpl api;

	@Test
	public void executeSearch() {
		String term = "ugly sweater";
		SearchResult result = api.executeSearch(term);
		assert(result.getItems().size() == 12);
	}

	@Test
	public void contextLoads() {
	}

}
