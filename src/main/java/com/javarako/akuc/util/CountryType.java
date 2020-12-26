package com.javarako.akuc.util;

public enum CountryType {

	CA("Canada"), 
	US("United States");
	
    private String fullname;

    CountryType(String fullname) {
        this.fullname = fullname;
    }

    public String fullname() {
        return fullname;
    }
}
