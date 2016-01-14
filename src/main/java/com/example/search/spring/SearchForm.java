package com.example.search.spring;

import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * Form-backing POJO for the search bar on the UI.
 *
 * Created by Kerrie on 1/4/2016.
 */
public class SearchForm {

    private String term;

    public String getTerm() { return term; }
    public void setTerm(String term) {
        this.term = term;
    }

}
