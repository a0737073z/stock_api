package com.example.connectdb.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResultSum {
    private List<FindStockResponsePlusList> resultList;

    private String responseCode;

    private String message;

}
