package com.example.connectdb.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FindStockRequest {
    //輸入查詢資料
    private String branchNo;

    private String custSeq;

    private String stock;

}
