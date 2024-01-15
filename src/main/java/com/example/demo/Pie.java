package com.example.demo;

import static com.example.demo.BsdBeApplication.prices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pie {

    List<PieSlice> pieSlices;

    public Pie() {
        pieSlices = new ArrayList<>();
    }

    public List<PieSlice> getPieSlices() {
        return pieSlices;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        Map<String, Double> gains = new HashMap<>();
        pieSlices.forEach(slice -> gains.put(slice.ticker(), slice.shares() * prices.get(slice.ticker())));
        Double sumGains = gains.values().stream().reduce(0.0, Double::sum);

        builder.append("Pie\n");
        pieSlices.forEach(slice -> {
            builder.append("\t").append(slice.ticker()).append("\n\tInvested: ").append(slice.invested_money())
                .append("$\n\tGained: ").append(gains.get(slice.ticker())).append("$\n\t");
            System.out.println(gains.get(slice.ticker()) / sumGains);
            for (int i = 0; i < Math.round(10 * gains.get(slice.ticker()) / sumGains); i++) {
                builder.append("-");
            }
            builder.append("\n");
        });

        return builder.toString();
    }
}
