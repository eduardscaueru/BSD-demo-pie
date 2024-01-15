package com.example.demo.repository;

import com.example.demo.model.SingleStockTransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TradeInfoRepository extends JpaRepository<SingleStockTransactionModel, Long> {
    @Query("SELECT t FROM SingleStockTransactionModel t WHERE t.userId = ?1 AND t.ticker = ?2")
    public List<SingleStockTransactionModel> findAllByUserIdAndTicker(Long userId, String ticker);
}
