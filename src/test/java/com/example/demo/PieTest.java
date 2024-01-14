package com.example.demo;

import com.example.demo.model.PieSliceModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PieTest {

    static Pie pie;

    @BeforeAll
    static void setUp() {

        try {
            BsdBeApplication.prices = new HashMap<>();
            List<Price> priceList = new ObjectMapper().readValue(
                new File("src/test/resources/prices.json"),
                new TypeReference<List<Price>>() {
                });
            priceList.forEach(price -> BsdBeApplication.prices.put(price.companyAbvr(), price.price()));
            System.out.println("Prices " + BsdBeApplication.prices);
        } catch (IOException e) {
            e.getMessage();
        }

        pie = new Pie();
        pie.getPieSlices().add(new PieSliceModel(2L, 1L, "PieName", "AAPL", 100.0, 50.2));
        pie.getPieSlices().add(new PieSliceModel(1L, 1L, "PieName", "ADBE", 825.12, 3.4));
    }

    @Test
    void printPieTest() {
        System.out.println(pie);
    }
}
