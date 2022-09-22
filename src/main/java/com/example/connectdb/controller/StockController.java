package com.example.connectdb.controller;

import com.example.connectdb.controller.dto.request.FindProfitMarginRequest;
import com.example.connectdb.controller.dto.request.FindStockRequest;
import com.example.connectdb.controller.dto.request.NewStockRequest;
import com.example.connectdb.controller.dto.request.UpdataMstmbNowPrice;
import com.example.connectdb.controller.dto.response.*;
import com.example.connectdb.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/unreal")
public class StockController {
    @Autowired
    private StockService stockService;

//    @PostMapping()
//    public List<FindStockResponse> getStockData(String branchNo,String custSeq,String stock) {
//        List<FindStockResponse> response = stockService.getStockData(branchNo,custSeq,stock);
//        return response;
//    }
    //第一題
    //----------------------------------------------------------------------------------------
    @PostMapping("/detail")
    public SerachResult getStockData(@RequestBody FindStockRequest request) {
        try {
            SerachResult response = stockService.getStockData(request);
            return response;
        }catch(Exception e){
            List<FindStockResponse> entity = new ArrayList<>();
            SerachResult result = new SerachResult();
            result.setResultList(entity);
            result.setResponseCode("005");
            result.setMessage("伺服器忙碌中，請稍後嘗試");
            return result;
        }
    }
    //第二題
    //-------------------------------------------------------------------------------------------
    @PostMapping("/sum")
    public ResultSum getStockDataAndList(@RequestBody FindStockRequest request) {
        try {
            ResultSum response = stockService.getStockDataAndList(request);
            return response;
        }catch (Exception e){
            List<FindStockResponsePlusList> entity = new ArrayList<>();
            ResultSum result = new ResultSum();
            result.setResultList(entity);
            result.setResponseCode("005");
            result.setMessage("伺服器忙碌中，請稍後嘗試");
            return result;
        }
    }
    //第三題
    //------------------------------------------------------------------------------------------
    @PostMapping("/add")
    public SerachResult newStock(@RequestBody NewStockRequest request) {
        try {
            SerachResult response = stockService.newStock(request);
            return response;
        }catch (Exception e){
            List<FindStockResponse> entity = new ArrayList<>();
            SerachResult result = new SerachResult();
            result.setResultList(entity);
            result.setResponseCode("005");
            result.setMessage("伺服器忙碌中，請稍後嘗試");
            return result;
        }
    }
    //更新現價
    //-----------------------------------------------------------------------------------------
    @PostMapping("/update")
    public UpdateResult updateMstmb(@RequestBody UpdataMstmbNowPrice request) {
        try {
            UpdateResult response = stockService.updateMstmb(request);
            return response;
        }catch (Exception e){
            List<BigDecimal> entity = new ArrayList<>();
            UpdateResult result = new UpdateResult();
            result.setNowPrice(entity);
            result.setResponseCode("005");
            result.setMessage("伺服器忙碌中，請稍後嘗試");
            return result;
        }
    }
//--------------------------------------------------------------------------------
//第一題增加獲利區間
@PostMapping("/limit")
public ProfitMargin getStockData1(@RequestBody FindProfitMarginRequest request) {
    try {
        ProfitMargin response = stockService.getStockData1(request);
        return response;
    }catch(Exception e){
        List<FindProfitMarginResponse> entity = new ArrayList<>();
        ProfitMargin result = new ProfitMargin();
        result.setResultList(entity);
        result.setResponseCode("005");
        result.setMessage("伺服器忙碌中，請稍後嘗試");
        return result;
    }
}
  }

