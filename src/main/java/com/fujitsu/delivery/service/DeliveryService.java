package com.fujitsu.delivery.service;

import com.fujitsu.delivery.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fujitsu.delivery.entity.Station;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service
public class DeliveryService {

    @Autowired
    WeatherService weatherService;

    private static final Map<String, Map<String, BigDecimal>> REGIONAL_BASE_FEES =
            Map.of("Tallinn", Map.of("Car", BigDecimal.valueOf(4), "Scooter", BigDecimal.valueOf(3.5), "Bike", BigDecimal.valueOf(3)),
                    "Tartu", Map.of("Car", BigDecimal.valueOf(3.5), "Scooter", BigDecimal.valueOf(3), "Bike", BigDecimal.valueOf(2.5)),
                    "Pärnu", Map.of("Car", BigDecimal.valueOf(3), "Scooter", BigDecimal.valueOf(2.5), "Bike", BigDecimal.valueOf(2)));


    /**
     * Calculates the total delivery fees based on the city and vehicle type.
     * Adds together the regional base fees and extra fees for weather conditions to get the total delivery fees.
     *
     * @param city the name of the city for which to calculate the delivery fees for
     * @param vehicleType the type of vehicle used for deliveries
     * @return the total delivery fees
     */
    public BigDecimal getDeliveryFees(String city, String vehicleType) {
        BigDecimal regionalBaseFees = getRegionalBaseFees(city, vehicleType);
        BigDecimal extraFees = calculateExtraFees(city, vehicleType);
        return regionalBaseFees.add(extraFees);
    }

    /**
     * Returns regional base fees based on city and vehicle type.
     * If the given city or vehicle type isn't in the regional base fees map, then an exception will be thrown.
     *
     * @param city the name of the city for which to get the regional base fees for
     * @param vehicleType the type of vehicle used for deliveries
     * @return the regional base fees for the given city and vehicle type.
     */
    public BigDecimal getRegionalBaseFees(String city, String vehicleType) {
        Map<String, BigDecimal> cityFeesData = REGIONAL_BASE_FEES.get(city);
        if (cityFeesData == null) {
            throw new ApplicationException("Entered city is unavailable");
        }
        BigDecimal regionalBaseFees = cityFeesData.get(vehicleType);
        if (regionalBaseFees == null) {
            throw new ApplicationException("Entered vehicle type is unavailable");
        }
        return regionalBaseFees;
    }


    /**
     * Calculates extra fees for weather conditions.
     * If the vehicle type is a bike or a scooter, then the extra fees for air temperature and weather phenomenon should be calculated.
     * If the vehicle type is a bike, then the extra fees for wind speed should also be calculated.
     *
     * @param city the name of the city for which to calculate the extra fees for
     * @param vehicleType the type of vehicle used for deliveries
     * @return the total extra fees for weather conditions
     */
    public BigDecimal calculateExtraFees(String city, String vehicleType) {
        BigDecimal extraFees = BigDecimal.ZERO;
        Station currentWeatherData = weatherService.getCurrentWeatherData(city);
        if (vehicleType.equals("Scooter") || vehicleType.equals("Bike")) {
            extraFees = extraFees.add(getAirTemperatureExtraFees(currentWeatherData));
            extraFees = extraFees.add(getWeatherPhenomenonExtraFees(currentWeatherData));
            if (vehicleType.equals("Bike")) {
                extraFees = extraFees.add(getWindSpeedExtraFees(currentWeatherData));
            }
        }
        return extraFees;
    }

    /**
     * Calculates extra fees based on air temperature.
     * If the air temperature is less than -10 degrees, then the air temperature extra fees should be 1 euro.
     * If the air temperature is between 0 and -10 degrees, then the air temperature extra fees should be 0.5 euros.
     *
     * @param currentWeatherData weather station object with the latest weather data
     * @return the extra fees based on air temperature
     */
    public BigDecimal getAirTemperatureExtraFees(Station currentWeatherData) {
        BigDecimal extraFees = BigDecimal.ZERO;
        double airTemperature = currentWeatherData.getAirTemperature();
        if (airTemperature <= 0) {
            extraFees = extraFees.add(BigDecimal.valueOf(0.5));
            if (airTemperature < -10) {
                extraFees = extraFees.add(BigDecimal.valueOf(0.5));
            }
        }
        return extraFees;
    }

    /**
     * Calculates extra fees based on wind speed.
     * If the wind speed is between 10 and 20 m/s, then the wind speed extra fees should be 0.5 euros.
     * If the wind speed is greater than 20 m/s, then an exception with the error message "Usage of
     * selected vehicle type is forbidden" has to be thrown.
     *
     * @param currentWeatherData weather station object with the latest weather data
     * @return the extra fees based on wind speed
     */
    public BigDecimal getWindSpeedExtraFees(Station currentWeatherData) {
        double windSpeed = currentWeatherData.getWindSpeed();
        if (windSpeed <= 20 && windSpeed >= 10) {
            return BigDecimal.valueOf(0.5);
        } else if (windSpeed > 20) {
            throw new ApplicationException("Usage of selected vehicle type is forbidden");
        }
        return BigDecimal.ZERO;
    }

    /**
     * Calculates extra fees based on weather phenomenon.
     * If the weather phenomenon is related to snow or sleet, then the weather phenomenon extra fees should be 1 euro.
     * If the weather phenomenon is related to rain, then the weather phenomenon extra fees should be 0.5 euros.
     * If the weather phenomenon is glaze, hail, or thunder, then an exception with the error message "Usage of
     * selected vehicle type is forbidden" has to be thrown.
     *
     * @param currentWeatherData weather station object with the latest weather data
     * @return the extra fees based on weather phenomenon
     */
    public BigDecimal getWeatherPhenomenonExtraFees(Station currentWeatherData) {
        String weatherPhenomenon = currentWeatherData.getPhenomenon();
        if (weatherPhenomenon != null) {
            weatherPhenomenon = weatherPhenomenon.toLowerCase();
            if (weatherPhenomenon.contains("snow") || weatherPhenomenon.contains("sleet")) {
                return BigDecimal.ONE;
            } else if (weatherPhenomenon.contains("rain") || List.of("light shower", "moderate shower", "heavy shower").contains(weatherPhenomenon)) {
                return BigDecimal.valueOf(0.5);
            } else if (weatherPhenomenon.equals("glaze") || weatherPhenomenon.equals("hail") || weatherPhenomenon.equals("thunder")) {
                throw new ApplicationException("Usage of selected vehicle type is forbidden");
            }
        }
        return BigDecimal.ZERO;
    }
}
