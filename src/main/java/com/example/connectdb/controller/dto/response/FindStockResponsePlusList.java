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
public class FindStockResponsePlusList {

    private String stock;

    private String stockName;

    private BigDecimal nowPrice;

    private BigDecimal sumRemainQty;

    private BigDecimal sumfee;

    private BigDecimal sumCost;

    private BigDecimal sumMarketValue;

    private BigDecimal sumUnrealProfit;

    private List<FindStockResponse> detailList;
}
