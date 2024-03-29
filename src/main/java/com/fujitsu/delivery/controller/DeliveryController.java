package com.fujitsu.delivery.controller;

import com.fujitsu.delivery.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping(value = "getDeliveryFees")
    public BigDecimal getDeliveryFees(@RequestParam(value = "city") String city,
                                      @RequestParam(value = "vehicleType") String vehicleType) {
        return deliveryService.getDeliveryFees(city, vehicleType);
    }
}
