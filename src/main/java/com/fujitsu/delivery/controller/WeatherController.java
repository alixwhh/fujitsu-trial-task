package com.fujitsu.delivery.controller;

import com.fujitsu.delivery.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @PostMapping(value = "saveWeather")
    public void saveWeather() {
        weatherService.saveWeather();
    }
}
