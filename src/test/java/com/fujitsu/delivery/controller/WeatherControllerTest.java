package com.fujitsu.delivery.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void whenSaveWeather_thenStatusIsOKAndDataIsAddedToDatabase() throws Exception{
        mvc.perform(post("/api/delivery/saveWeather"))
                .andDo(print()).andExpect(status().isOk());
        mvc.perform(get("/api/delivery/getDeliveryFees?city=Tartu&vehicleType=Bike"))
                .andExpect(status().isOk());
    }
}
