package com.javarako.akuc.util;

public enum ProvinceType {

	NL("Newfoundland and Labrador"), 
	PE("Prince Edward Island"), 
	NS("Nova Scotia"), 
	NB("New Brunswick"), 
	QC("Quebec"), 
	ON("Ontario"), 
	MB("Manitoba"), 
	SK("Saskatchewan"), 
	AB("Alberta"), 
	BC("British Columbia"), 
	YT("Yukon"), 
	NT("Northwest Territories"), 
	NU("Nunavut");
	
    private String fullname;

    ProvinceType(String fullname) {
        this.fullname = fullname;
    }

    public String fullname() {
        return fullname;
    }
}
