package com.fujitsu.delivery.service;

import com.fujitsu.delivery.entity.WeatherStation;
import com.fujitsu.delivery.exception.ApplicationException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private DeliveryService deliveryService;

    public static final WeatherStation WEATHER_STATION = WeatherStation.builder().name("Tartu-Tõravere").WMOCode(26242)
            .airTemperature(-2.1).windSpeed(4.7).phenomenon("Light snow shower").build();

    public static final WeatherStation WEATHER_STATION_WITH_STRONG_WIND = WeatherStation.builder().name("Tartu-Tõravere").WMOCode(26242)
            .airTemperature(-2.1).windSpeed(20.5).phenomenon("Light snow shower").build();

    public static final WeatherStation WEATHER_STATION_WITH_DANGEROUS_WEATHER_PHENOMENON = WeatherStation.builder().name("Tartu-Tõravere").WMOCode(26242)
            .airTemperature(-2.1).windSpeed(4.7).phenomenon("Thunder").build();
    private static final BigDecimal STATION_DELIVERY_FEES = BigDecimal.valueOf(4.0);

    @Test
    void givenCityAndVehicleType_whenGetDeliveryFees_thenShouldReturnCorrectDeliveryFees() {
        given(weatherService.getCurrentWeatherData("Tartu")).willReturn(WEATHER_STATION);

        var result = deliveryService.getDeliveryFees("Tartu", "Bike");

        then(weatherService).should().getCurrentWeatherData("Tartu");
        assertEquals(STATION_DELIVERY_FEES, result);
    }

    @Test
    void givenCityAndIncorrectVehicleType_whenGetDeliveryFees_thenShouldReturnVehicleTypeUnavailableErrorMessage() {
        Exception actualException = assertThrows(ApplicationException.class, () ->
                deliveryService.getDeliveryFees("Tartu", "Bus"));
        assertThat(actualException.getMessage()).isEqualTo("Entered vehicle type is unavailable");
    }

    @Test
    void givenIncorrectCityAndVehicleType_whenGetDeliveryFees_thenShouldReturnCityUnavailableErrorMessage() {
        Exception actualException = assertThrows(ApplicationException.class, () ->
                deliveryService.getDeliveryFees("Saue", "Bike"));
        assertThat(actualException.getMessage()).isEqualTo("Entered city is unavailable");
    }

    @Test
    void givenCityAndVehicleTypeAndStrongWind_whenGetDeliveryFees_thenShouldReturnVehicleTypeForbiddenErrorMessage() {
        given(weatherService.getCurrentWeatherData("Tartu")).willReturn(WEATHER_STATION_WITH_STRONG_WIND);

        Exception actualException = assertThrows(ApplicationException.class, () ->
                deliveryService.getDeliveryFees("Tartu", "Bike"));
        assertThat(actualException.getMessage()).isEqualTo("Usage of selected vehicle type is forbidden");
    }

    @Test
    void givenCityAndVehicleTypeAndDangerousWeatherPhenomenon_whenGetDeliveryFees_thenShouldReturnVehicleTypeForbiddenErrorMessage() {
        given(weatherService.getCurrentWeatherData("Tartu")).willReturn(WEATHER_STATION_WITH_DANGEROUS_WEATHER_PHENOMENON);

        Exception actualException = assertThrows(ApplicationException.class, () ->
                deliveryService.getDeliveryFees("Tartu", "Bike"));
        assertThat(actualException.getMessage()).isEqualTo("Usage of selected vehicle type is forbidden");
    }
}