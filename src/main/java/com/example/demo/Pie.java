package com.example.demo;

import com.example.demo.model.PieSliceModel;

import static com.example.demo.BsdBeApplication.prices;

import com.example.demo.model.PieSliceModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pie {

    List<PieSliceModel> pieSlices;

    public Pie() {
        pieSlices = new ArrayList<>();
    }

    public List<PieSliceModel> getPieSlices() {
        return pieSlices;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        Map<String, Double> gains = new HashMap<>();
        pieSlices.forEach(slice -> gains.put(slice.getTicker(), slice.getShares() * prices.get(slice.getTicker())));
        Double sumGains = gains.values().stream().reduce(0.0, Double::sum);

        builder.append("Pie\n");
        pieSlices.forEach(slice -> {
            builder.append("\t").append(slice.getTicker()).append("\n\tInvested: ").append(slice.getInvestedMoney())
                .append("$\n\tGained: ").append(gains.get(slice.getTicker())).append("$\n\t");
            System.out.println(gains.get(slice.getTicker()) / sumGains);
            for (int i = 0; i < Math.round(10 * gains.get(slice.getTicker()) / sumGains); i++) {
                builder.append("-");
            }
            builder.append("\n");
        });

        return builder.toString();
    }
}
