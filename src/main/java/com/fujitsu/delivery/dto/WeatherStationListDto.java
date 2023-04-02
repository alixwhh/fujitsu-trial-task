package com.fujitsu.delivery.dto;

import jakarta.xml.bind.annotation.*;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@XmlRootElement(name="observations")
@XmlAccessorType(XmlAccessType.FIELD)
public class WeatherStationListDto {
    @XmlElement(name="station")
    private List<WeatherStationDTO> stations;

    @XmlAttribute(name="timestamp")
    private int timestamp;
}
