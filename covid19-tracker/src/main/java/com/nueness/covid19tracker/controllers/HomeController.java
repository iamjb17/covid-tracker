package com.nueness.covid19tracker.controllers;

import java.util.List;

import com.nueness.covid19tracker.services.Covid19DataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nueness.covid19tracker.models.LocationsStats;

// thymeleaf makes this possible to work as easy as it does
// Controllers are used for the UI/rendering to the screen. How the user interacts with the application
// Not a REST controller. REST controller(all methods return REST responses) are used for APIs or other interactions that are not UI
@Controller
public class HomeController {

    @Autowired
    Covid19DataService  covid19DataService;
    
    // The Model is data/objects passed in so that it can be accessed or used in the HTML
    // GetMapping() helps with the url mapping
    @GetMapping("/")
    public String home(Model model) {
        List<LocationsStats> allStats = covid19DataService.getAllStats();
        
        int totalCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalDeltaCases = allStats.stream().mapToInt(stat -> stat.getDiffFromLastDate()).sum();

        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCasesToday", totalCases);
        model.addAttribute("totalDeltaCases", totalDeltaCases);
        return "home";
    }
}
