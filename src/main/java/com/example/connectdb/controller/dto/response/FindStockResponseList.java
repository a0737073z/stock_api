package com.example.connectdb.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FindStockResponseList {
    private String stock;

    private String stockName;

    private double nowPrice;

    private double sumRemainQty;

    private double sumfee;

    private double sumCost;

    private double sumMarketValue;

    private double sumUnrealProfit;
}
