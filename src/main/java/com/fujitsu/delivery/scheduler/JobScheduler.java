package com.fujitsu.delivery.scheduler;

import com.fujitsu.delivery.controller.WeatherController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

@Component
@RequiredArgsConstructor
public class JobScheduler {

    @Autowired
    private WeatherController weatherController;

    @Scheduled(cron = "0 15 * * * *")
    public void scheduleSaveWeatherData() {
        weatherController.saveWeather();
    }
}