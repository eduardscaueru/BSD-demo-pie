package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Price(@JsonProperty("Company Name") String companyName, @JsonProperty("Company Abvr") String companyAbvr, @JsonProperty("Price") Double price) {
}
