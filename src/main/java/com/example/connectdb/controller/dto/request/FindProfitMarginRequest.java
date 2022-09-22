package com.example.connectdb.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FindProfitMarginRequest {
    private String branchNo;

    private String custSeq;

    private String stock;

    private double topLimit;

    private double lowLimit;
}
