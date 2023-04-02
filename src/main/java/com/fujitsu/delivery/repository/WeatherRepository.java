package com.fujitsu.delivery.repository;

import com.fujitsu.delivery.entity.WeatherStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WeatherRepository extends JpaRepository<WeatherStation, Integer> {
    WeatherStation findFirstByNameContainingIgnoreCaseOrderByIdDesc(String city);
}

