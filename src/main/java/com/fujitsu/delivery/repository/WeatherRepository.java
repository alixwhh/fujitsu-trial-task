package com.fujitsu.delivery.repository;

import com.fujitsu.delivery.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WeatherRepository extends JpaRepository<Station, Integer> {
    Station findFirstByNameContainingIgnoreCaseOrderByIdDesc(String city);
}

