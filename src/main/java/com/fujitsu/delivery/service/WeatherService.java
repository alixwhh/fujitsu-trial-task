package com.fujitsu.delivery.service;

import com.fujitsu.delivery.dto.WeatherStationDTO;
import com.fujitsu.delivery.dto.WeatherStationListDto;
import com.fujitsu.delivery.repository.WeatherRepository;
import com.fujitsu.delivery.util.ModelMapperFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fujitsu.delivery.entity.WeatherStation;

import java.util.List;
import java.util.Objects;

@Service
public class WeatherService {
    private static final String WEATHER_PORTAL_URL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
    private static final List<String> OBSERVED_STATIONS = List.of("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");

    @Autowired
    private WeatherRepository weatherRepository;

    /**
     * Saves the weather station with all the weather data to the database.
     *
     * @param weatherStationDTO DTO of the weather station that will be saved
     */
    public void save(WeatherStationDTO weatherStationDTO) {
        ModelMapper modelMapper = ModelMapperFactory.getMapper();
        weatherRepository.save(modelMapper.map(weatherStationDTO, WeatherStation.class));
    }

    /**
     * Saves weather data for the observed stations to the database.
     */
    public void saveWeather() {
        RestTemplate restTemplate = new RestTemplate();
        WeatherStationListDto response = restTemplate.getForObject(WEATHER_PORTAL_URL, WeatherStationListDto.class);
        for (WeatherStationDTO weatherStationDTO : Objects.requireNonNull(response).getStations()) {
            if (OBSERVED_STATIONS.contains(weatherStationDTO.getName())) {
                weatherStationDTO.setTimestamp(response.getTimestamp());
                save(weatherStationDTO);
            }
        }
    }

    /**
     * Finds the latest weather data for the given city.
     *
     * @return weather station with the most recent weather data for the given city
     */

    public WeatherStation getCurrentWeatherData(String city) {
        return weatherRepository.findFirstByNameContainingIgnoreCaseOrderByIdDesc(city);
    }
}
