package com.nueness.covid19tracker.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nueness.covid19tracker.models.LocationsStats;

// Service decorator tells spring boot to create an instance of the class at startup
@Service
public class Covid19DataService {

    // constant(capitalized) string variable of the url for the data
    private static String COVID_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    
    // Will hold the LocationStats obj. Dont want to create state inside of services for the most part, but we are doing it anyway.
    private List<LocationsStats> allStats = new ArrayList<>();

    public List<LocationsStats> getAllStats() {
        return this.allStats;
    }

    // PostConstruct tells spring boot to execute this method when class is init
    // send request to web to get the data as a string
    // Scheduled makes this function run ever N time(cron sec min hour)(more accurate way to do this)
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchCovidData() throws IOException, InterruptedException {
        // for concurrency reasons we are creating a new stats container everytime, so as not to stall out the service if multiple people use it at the same time.
        List<LocationsStats> newStats = new ArrayList<>();
        // create a http client and send the request to url
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(COVID_DATA_URL)).build();

        // the response from the url. String.
        /* 
            the client sends the request. httpresponse is not created directed but returned from request. Bodyhandlers has a bunch
            of different methods that can handle the request. Will not examine the body but will always accept it, if you want to
            examine body before accepted, create a custom handler.
        */
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        // System.out.println(httpResponse.body());

        StringReader csvBodyReader = new StringReader(httpResponse.body());
        
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records) {
            LocationsStats locationsStat = new LocationsStats();
            locationsStat.setState(record.get("Province/State"));
            locationsStat.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size()-1));
            int prevLatestCases = Integer.parseInt(record.get(record.size()-2));
            locationsStat.setLatestTotalCases(latestCases);
            locationsStat.setDiffFromLastDate(latestCases - prevLatestCases);
            newStats.add(locationsStat);
        }

        this.allStats = newStats;
    }
}
