package com.fujitsu.delivery.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DeliveryControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void givenCityAndIncorrectVehicleType_whenGetDeliveryFees_thenStatusIsBadRequestAndErrorMessageIsGiven() throws Exception{
        mvc.perform(post("/api/delivery/saveWeather"))
                .andDo(print()).andExpect(status().isOk());
        mvc.perform(get("/api/delivery/getDeliveryFees?city=Tartu&vehicleType=Bus"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Entered vehicle type is unavailable"));
    }

    @Test
    void givenIncorrectCityAndVehicleType_whenGetDeliveryFees_thenStatusIsBadRequestAndErrorMessageIsGiven() throws Exception{
        mvc.perform(post("/api/delivery/saveWeather"))
                .andDo(print()).andExpect(status().isOk());
        mvc.perform(get("/api/delivery/getDeliveryFees?city=Keila&vehicleType=Bike"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Entered city is unavailable"));
    }
}
