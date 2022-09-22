package com.example.connectdb.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResult {

    private List<BigDecimal> nowPrice;

    private String responseCode;

    private String message;
}
