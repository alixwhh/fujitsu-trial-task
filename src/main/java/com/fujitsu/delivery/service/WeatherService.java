package com.fujitsu.delivery.service;

import com.fujitsu.delivery.dto.StationDTO;
import com.fujitsu.delivery.dto.StationListDto;
import com.fujitsu.delivery.repository.WeatherRepository;
import com.fujitsu.delivery.util.ModelMapperFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fujitsu.delivery.entity.Station;

import java.util.List;
import java.util.Objects;

@Service
public class WeatherService {
    private static final String WEATHER_PORTAL_URL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
    private static final List<String> OBSERVED_STATIONS = List.of("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");

    @Autowired
    private WeatherRepository weatherRepository;

    public void save(StationDTO stationDTO) {
        ModelMapper modelMapper = ModelMapperFactory.getMapper();
        weatherRepository.save(modelMapper.map(stationDTO, Station.class));
    }

    public void saveWeather() {
        RestTemplate restTemplate = new RestTemplate();
        StationListDto response = restTemplate.getForObject(WEATHER_PORTAL_URL, StationListDto.class);
        for (StationDTO stationDTO: Objects.requireNonNull(response).getStations()) {
            if (OBSERVED_STATIONS.contains(stationDTO.getName())) {
                stationDTO.setTimestamp(response.getTimestamp());
                save(stationDTO);
            }
        }
    }

    public Station getCurrentWeatherData(String city) {
        return weatherRepository.findFirstByNameContainingIgnoreCaseOrderByIdDesc(city);
    }
}
