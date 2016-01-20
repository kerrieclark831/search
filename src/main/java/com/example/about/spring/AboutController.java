package com.example.about.spring;

import com.example.about.api.AboutAPIImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * Spring MVC controller for toy about application that demonstrates
 * the code structure for SHOP.COM bulkhead applications.
 *
 * Created by Kerrie on 1/3/2016.
 */
@Controller
public class AboutController {

    @Autowired
    private AboutAPIImpl api;

    @RequestMapping(path = "/about-us", method = RequestMethod.GET)
    public String getAboutUs(ModelMap modelMap) {
        return "about-us";
    }

    @RequestMapping(path = "/our-brands", method = RequestMethod.GET)
    public String getOurBrands(ModelMap modelMap) {
        return "our-brands";
    }

}
