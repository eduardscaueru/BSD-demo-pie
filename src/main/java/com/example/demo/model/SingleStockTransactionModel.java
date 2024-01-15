package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trade_info")
public class SingleStockTransactionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id", updatable = false, nullable = false)
    private Long tradeId;

    @Column(name = "user_id", updatable = false, nullable = false)
    private Long userId;

    @Column(name = "pie_name", updatable = false, nullable = false)
    private String pieName;

    @Column(name = "stock_name", updatable = false, nullable = false)
    private String stockName;

    @Column(name = "ticker", updatable = false, nullable = false)
    private String ticker;

    @Column(name = "buy_sell", updatable = false, nullable = false)
    private String buySell;

    @Column(name = "value", updatable = false, nullable = false)
    private Double value;

    @Column(name = "shares", updatable = false, nullable = false)
    private Double shares;

    @Column(name = "timestamp", updatable = false, nullable = false)
    private Long timestamp;
}
