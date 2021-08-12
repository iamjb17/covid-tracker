package com.nueness.covid19tracker.models;

public class LocationsStats {
    
    private String state;
    private String country;
    private int latestTotalCases;
    private int diffFromLastDate;

    
    // public LocationsStats(String state, String country, int latestTotalCases) {
    //     this.state = state;
    //     this.country = country;
    //     this.latestTotalCases = latestTotalCases;
    // }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getLatestTotalCases() {
        return this.latestTotalCases;
    }

    public void setLatestTotalCases(int latestTotalCases) {
        this.latestTotalCases = latestTotalCases;
    }

    public int getDiffFromLastDate() {
        return this.diffFromLastDate;
    }

    public void setDiffFromLastDate(int diffFromLastDate) {
        this.diffFromLastDate = diffFromLastDate;
    }


    @Override
    public String toString() {
        return "{" +
            " state='" + getState() + "'" +
            ", country='" + getCountry() + "'" +
            ", latestTotalCases='" + getLatestTotalCases() + "'" +
            "}";
    }


}
