package com.example.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pie {

    List<PieSlice> pieSlices;
    Map<String, Double> prices;

    public Pie() {

        try {
            prices = new HashMap<>();
            List<Price> priceList = new ObjectMapper().readValue(
                new File("src/test/resources/prices.json"),
                new TypeReference<List<Price>>() {
                });
            priceList.forEach(price -> prices.put(price.companyAbvr(), price.price()));
            System.out.println("Prices " + prices);
        } catch (IOException e) {

        }
        pieSlices = new ArrayList<>();
    }

    public List<PieSlice> getPieSlices() {
        return pieSlices;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        Map<String, Double> gains = new HashMap<>();
        pieSlices.forEach(slice -> gains.put(slice.companyAbvr(), slice.shares() * prices.get(slice.companyAbvr())));
        Double sumGains = gains.values().stream().reduce(0.0, Double::sum);

        builder.append("Pie\n");
        pieSlices.forEach(slice -> {
            builder.append("\t").append(slice.companyAbvr()).append("\n\tInvested: ").append(slice.investedMoney())
                .append("$\n\tGained: ").append(gains.get(slice.companyAbvr())).append("$\n\t");
            System.out.println(gains.get(slice.companyAbvr()) / sumGains);
            for (int i = 0; i < Math.round(10 * gains.get(slice.companyAbvr()) / sumGains); i++) {
                builder.append("-");
            }
            builder.append("\n");
        });

        return builder.toString();
    }
}
