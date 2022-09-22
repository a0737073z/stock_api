package com.example.connectdb.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewStockRequest {
        private String tradeDate;

        private String branchNo;

        private String custSeq;

        private String docSeq;

        private String stock;

        private Double price;

        private Double qty;
}
