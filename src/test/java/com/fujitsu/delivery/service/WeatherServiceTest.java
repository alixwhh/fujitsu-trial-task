package com.fujitsu.delivery.service;

import com.fujitsu.delivery.entity.WeatherStation;
import com.fujitsu.delivery.repository.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private WeatherService weatherService;

    public static final WeatherStation WEATHER_STATION = WeatherStation.builder().name("Tartu-Tõravere").WMOCode(26242)
            .airTemperature(-2.1).windSpeed(4.7).phenomenon("Light snow shower").build();


    @Test
    void givenCity_whenGetCurrentWeatherData_thenShouldReturnStation() {
        given(weatherRepository.findFirstByNameContainingIgnoreCaseOrderByIdDesc("Tartu")).willReturn(WEATHER_STATION);

        var result = weatherService.getCurrentWeatherData("Tartu");

        then(weatherRepository).should().findFirstByNameContainingIgnoreCaseOrderByIdDesc("Tartu");
        assertEquals(WEATHER_STATION, result);
    }
}
