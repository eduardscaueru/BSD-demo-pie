package com.example.demo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PieTest {

    static Pie pie;

    @BeforeAll
    static void setUp() {

        pie = new Pie();
//        pie.getPieSlices().add(new PieSlice("APPL", 100.0, 50.2));
//        pie.getPieSlices().add(new PieSlice("ADBE", 825.12, 3.4));
    }

    @Test
    void printPieTest() {
        System.out.println(pie);
    }
}
