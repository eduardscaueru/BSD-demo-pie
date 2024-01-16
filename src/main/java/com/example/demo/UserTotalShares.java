package com.example.demo;

public class UserTotalShares {

    Double shares;
    Double value;
    Double balance;

    public UserTotalShares(Double shares, Double value, Double balance) {
        this.shares = shares;
        this.value = value;
        this.balance = balance;
    }

    @Override
    public String toString() {
        String val = "UserDetails: {\n";
        val += "\t\"shares\":" + Double.toString(shares) + ",\n";
        val += "\t\"value\":" + Double.toString(value) + ",\n";
        val += "\t\"balance\":" + Double.toString(balance) + "\n";
        val += "}";
        return val;
    }
}
