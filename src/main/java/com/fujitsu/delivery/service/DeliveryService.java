package com.fujitsu.delivery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fujitsu.delivery.entity.Station;

import java.math.BigDecimal;
import java.util.Map;


@Service
public class DeliveryService {

    @Autowired
    WeatherService weatherService;

    private static final Map<String, Map<String, BigDecimal>> REGIONAL_BASE_FEES =
            Map.of("Tallinn", Map.of("Car", BigDecimal.valueOf(4), "Scooter", BigDecimal.valueOf(3.5), "Bike", BigDecimal.valueOf(3)),
                    "Tartu", Map.of("Car", BigDecimal.valueOf(3.5), "Scooter", BigDecimal.valueOf(3), "Bike", BigDecimal.valueOf(2.5)),
                    "Pärnu", Map.of("Car", BigDecimal.valueOf(3), "Scooter", BigDecimal.valueOf(2.5), "Bike", BigDecimal.valueOf(2)));


    public int getDeliveryFees(String city, String vehicleType) {
        BigDecimal regionalBaseFees = REGIONAL_BASE_FEES.get(city).get(vehicleType);
        BigDecimal extraFees = calculateExtraFees(city, vehicleType);
        return 0;
    }


    public BigDecimal calculateExtraFees(String city, String vehicleType) {
        BigDecimal extraFees = BigDecimal.ZERO;
        Station currentWeatherData = getCurrentWeatherData(city);
        if (vehicleType.equals("Scooter") || vehicleType.equals("Bike")) {
            extraFees = extraFees.add(getAirTemperatureExtraFees(currentWeatherData));
            if (vehicleType.equals("Bike")) {
                extraFees = extraFees.add(getWindSpeedExtraFees(currentWeatherData));
            }
        }
        return extraFees;
    }

    public BigDecimal getAirTemperatureExtraFees(Station currentWeatherData) {
        BigDecimal extraFees = BigDecimal.ZERO;
        double airTemperature = currentWeatherData.getAirTemperature();
        if (airTemperature < 0) {
            extraFees = extraFees.add(BigDecimal.valueOf(0.5));
            if (airTemperature < -10) {
                extraFees = extraFees.add(BigDecimal.valueOf(0.5));
            }
        }
        return extraFees;
    }

    public BigDecimal getWindSpeedExtraFees(Station currentWeatherData) {
        double windSpeed = currentWeatherData.getWindSpeed();
        if (windSpeed <= 20 && windSpeed >= 10) {
            return BigDecimal.valueOf(0.5);
        } else if (windSpeed > 20) {
            System.out.println("Usage of selected vehicle type is forbidden");
        }
        return BigDecimal.ZERO;
    }

    public Station getCurrentWeatherData(String city) {
        return weatherService.getCurrentWeatherData(city);
    }
}
