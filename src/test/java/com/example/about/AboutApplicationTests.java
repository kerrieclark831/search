package com.example.about;

import com.example.about.api.AboutAPIImpl;
import com.example.about.spring.AboutApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AboutApplication.class)
@WebAppConfiguration
public class AboutApplicationTests {

	@Autowired
	private AboutAPIImpl api;

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
