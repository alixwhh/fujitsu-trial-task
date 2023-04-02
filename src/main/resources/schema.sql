DROP TABLE IF EXISTS weather_station;
CREATE TABLE weather_station (
    id INT AUTO_INCREMENT PRIMARY KEY,
    station VARCHAR(255),
    wmo_code VARCHAR(255),
    air_temperature DOUBLE,
    wind_speed DOUBLE,
    weather_phenomenon VARCHAR(255),
    timestamp INT
);