package com.javarako.akuc.util;

public enum LocationCode {

	Location_1("1 구역"), 
	Location_2("2 구역"), 
	Location_3("3 구역"), 
	Location_4("4 구역"),
	Location_5("5 구역"), 
	Location_6("6 구역");
	
    private String description;

    LocationCode(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
