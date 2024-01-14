package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pie_slice_table")
public class PieSliceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pie_slice_id", updatable = false, nullable = false)
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
}

//    @OneToMany(mappedBy = "user")
//    private List<UserLogin> logins = new ArrayList<>();


