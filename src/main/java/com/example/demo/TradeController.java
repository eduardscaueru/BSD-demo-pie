package com.example.demo;

import static com.example.demo.BsdBeApplication.prices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TradeController {

    @Autowired
    private PieService pieService;

    @PostMapping("/pie")
    public ResponseEntity<String> addSlice(@RequestBody String body) {

        System.out.println("Pie: " + body);
        return ResponseEntity.ok(pieService.addSlice(body));
    }

    @PostMapping("/pie/{name}")
    public ResponseEntity<Pie> getPie(@PathVariable String name) {

        return ResponseEntity.ok(pieService.getPie(name));
    }

    @PostMapping("/updatePrices")
    public ResponseEntity<String> updatePrices(@RequestBody String body) {

        try {
            System.out.println(body);
            List<Price> priceList = new ObjectMapper().readValue(
                body,
                new TypeReference<List<Price>>() {
                });
            priceList.forEach(price -> prices.put(price.companyAbvr(), price.price()));
            System.out.println("Prices updated:\n" + prices);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().build();
    }
}
