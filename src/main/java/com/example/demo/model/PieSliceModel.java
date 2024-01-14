package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pie_slice_table")
public class PieSliceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pie_slice_id", updatable = true, nullable = false)
    private Long pieSliceId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "pie_name")
    private String pieName;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "invested_money")
    private Double investedMoney;

    @Column(name = "shares")
    private Double shares;

//    @OneToMany(mappedBy = "user")
//    private List<UserLogin> logins = new ArrayList<>();
}
