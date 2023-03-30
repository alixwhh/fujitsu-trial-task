package com.fujitsu.delivery.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="station")
@XmlAccessorType(XmlAccessType.FIELD)
public class StationDTO {
    private String name;
    @XmlElement(name="wmocode")
    private int WMOCode;
    @XmlElement(name="airtemperature")
    private double airTemperature;
    @XmlElement(name="windspeed")
    private double windSpeed;
    private String phenomenon;
    private int timestamp;
}
