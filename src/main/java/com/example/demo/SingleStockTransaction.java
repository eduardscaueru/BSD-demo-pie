package com.example.demo;

public record SingleStockTransaction(Long trade_id, Long user_id, String pie_name, String stock_name, String ticker, String buy_sell, Double value, Double shares, Long timestamp) {
}
