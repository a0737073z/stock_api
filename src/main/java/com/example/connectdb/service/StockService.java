package com.example.connectdb.service;

import com.example.connectdb.controller.dto.request.FindProfitMarginRequest;
import com.example.connectdb.controller.dto.request.FindStockRequest;
import com.example.connectdb.controller.dto.request.NewStockRequest;
import com.example.connectdb.controller.dto.request.UpdataMstmbNowPrice;
import com.example.connectdb.controller.dto.response.*;
import com.example.connectdb.model.HcmioRepository;
import com.example.connectdb.model.MstmbRepository;
import com.example.connectdb.model.TcnudRepository;
import com.example.connectdb.model.entity.hcmio.Hcmio;
import com.example.connectdb.model.entity.mstmb.Mstmb;
import com.example.connectdb.model.entity.tcnud.Tcnud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {

    @Autowired
    private HcmioRepository hcmioRepository;

    @Autowired
    private MstmbRepository mstmbRepository;

    @Autowired
    private TcnudRepository tcnudRepository;

    //--------------------------------------------------------------------------------------------------------
    //資料查詢==>第一題
    public SerachResult getStockData(FindStockRequest request) {
        String branchNo = request.getBranchNo();
        String custSeq = request.getCustSeq();
        String stock = request.getStock();
        List<Mstmb> mstmbList;
        List<Tcnud> tcnudList;
        if (null == stock) {
            tcnudList = tcnudRepository.findByBranchAndCustSeq(branchNo, custSeq);
            mstmbList = mstmbRepository.findAll();
        } else {
            mstmbList = mstmbRepository.findByStock(stock);
            tcnudList = tcnudRepository.findByBranchAndCustByAndStock(branchNo, custSeq, stock);
        }
        //判斷stock是否為空值
        List<FindStockResponse> findStockResponses = new ArrayList<>();
        SerachResult resultList = new SerachResult();
        for (Mstmb mstmb : mstmbList){
            for (Tcnud tcnud : tcnudList) {
                if (mstmb.getStock().equals(tcnud.getStock())) {
                    BigDecimal buyPrice = new BigDecimal(tcnud.getPrice()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal nowPrice = new BigDecimal(mstmb.getCurPrice()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal qty = new BigDecimal(tcnud.getQty()).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal remainQty = new BigDecimal(tcnud.getRemainQty()).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal fee = new BigDecimal(tcnud.getFee()).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal cost = new BigDecimal(Math.ceil((tcnud.getPrice() * tcnud.getQty()) + ((tcnud.getPrice() * tcnud.getQty()) * 0.001425))).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal marketValue = new BigDecimal((mstmb.getCurPrice() * tcnud.getRemainQty()) - Math.ceil((mstmb.getCurPrice() * tcnud.getRemainQty()) * 0.001425) - Math.ceil((mstmb.getCurPrice() * tcnud.getRemainQty()) * 0.003)).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal unrealProfit = new BigDecimal(((mstmb.getCurPrice() * tcnud.getRemainQty()) - Math.ceil((mstmb.getCurPrice() * tcnud.getRemainQty()) * 0.001425) - Math.ceil((mstmb.getCurPrice() * tcnud.getRemainQty()) * 0.003)) - ((tcnud.getPrice() * tcnud.getQty()) + Math.ceil((tcnud.getPrice() * tcnud.getQty()) * 0.001425))).setScale(0, RoundingMode.HALF_UP);
                    FindStockResponse findStockResponse = new FindStockResponse();
                    findStockResponse.setTradeDate(tcnud.getTradeDate());
                    findStockResponse.setDocSeq(tcnud.getDocSeq());
                    findStockResponse.setStock(tcnud.getStock());
                    findStockResponse.setStockName(mstmb.getStockName());
                    findStockResponse.setBuyPrice(buyPrice);
                    findStockResponse.setNowPrice(nowPrice);
                    findStockResponse.setQty(qty);
                    findStockResponse.setRemainQty(remainQty);
                    findStockResponse.setFee(fee);
                    findStockResponse.setCost(cost);
                    findStockResponse.setMarketValue(marketValue);
                    findStockResponse.setUnrealProfit(unrealProfit);
                    findStockResponses.add(findStockResponse);
                }
            }
        }



                if (request.getBranchNo() == null || request.getCustSeq() == null || (request.getBranchNo() == null && request.getCustSeq() == null)) {
                    resultList.setResponseCode("002");
                    resultList.setMessage("查無符合資料");
                }else if(tcnudRepository.findByBranchAndCustByAndStock(request.getBranchNo(),request.getCustSeq(),request.getStock()).isEmpty()){
                    resultList.setResponseCode("001");
                    resultList.setMessage("branchNo或CustSeq或stock資料輸入錯誤，請再次檢查");
                }
        for(Tcnud tcnud : tcnudList){
            if(request.getBranchNo().equals(tcnud.getBranchNo()) && request.getCustSeq().equals(tcnud.getCustSeq())){
                resultList.setResponseCode("000");
                resultList.setMessage("");
            }
        }

        resultList.setResultList(findStockResponses);
        return resultList;
    }

    //------------------------------------------------------------------------------------------------------
    //第二題
    public ResultSum getStockDataAndList(FindStockRequest request) {
        String branchNo = request.getBranchNo();
        String custSeq = request.getCustSeq();
        String stock = request.getStock();
        List<Mstmb> mstmbList;
        List<Tcnud> tcnudList;
        if (null == stock) {
            tcnudList = tcnudRepository.findByBranchAndCustSeq(branchNo, custSeq);
            mstmbList = mstmbRepository.findAll();
        } else {
            mstmbList = mstmbRepository.findByStock(stock);
            tcnudList = tcnudRepository.findByBranchAndCustByAndStock(branchNo, custSeq, stock);
        }

        ResultSum resultList = new ResultSum();
        List<FindStockResponsePlusList> findStockResponsePlusLists = new ArrayList<>();
        List<FindStockResponse> findStockResponseResponses = new ArrayList<>();


        double sumRemainQty = 0;
        double sumFee = 0;
        double sumCost = 0;
        double sumMarketValue = 0;
        double sumUnrealProfit = 0;
        for(Tcnud tcnud : tcnudList) {
            for (Mstmb mstmb : mstmbList) {
                if (tcnud.getStock().equals(mstmb.getStock())) {
                    sumRemainQty += tcnud.getRemainQty();
                    sumFee += tcnud.getFee();
                    sumCost += tcnud.getCost();
                    sumMarketValue += ((mstmb.getCurPrice() * tcnud.getRemainQty()) - Math.ceil((mstmb.getCurPrice() * tcnud.getRemainQty()) * 0.001425) - Math.ceil((mstmb.getCurPrice() * tcnud.getRemainQty()) * 0.003));
                    sumUnrealProfit += (((mstmb.getCurPrice() * tcnud.getRemainQty()) - Math.ceil((mstmb.getCurPrice() * tcnud.getRemainQty()) * 0.001425) - Math.ceil((mstmb.getCurPrice() * tcnud.getRemainQty()) * 0.003)) - ((tcnud.getPrice() * tcnud.getQty()) + Math.ceil((tcnud.getPrice() * tcnud.getQty()) * 0.001425)));
                }
            }
        }
        FindStockResponsePlusList findStockResponsePlusList = new FindStockResponsePlusList();
        for (Mstmb mstmb : mstmbList) {
            for (Tcnud tcnud : tcnudList) {
                if (mstmb.getStock().equals(tcnud.getStock())) {
                    BigDecimal nowPrice = new BigDecimal(mstmb.getCurPrice()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal buyPrice = new BigDecimal(tcnud.getPrice()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal qty = new BigDecimal(tcnud.getQty()).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal remainQty = new BigDecimal(tcnud.getRemainQty()).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal fee = new BigDecimal(tcnud.getFee()).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal cost = new BigDecimal(Math.ceil((tcnud.getPrice() * tcnud.getQty()) + ((tcnud.getPrice() * tcnud.getQty()) * 0.001425))).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal marketValue = new BigDecimal((mstmb.getCurPrice() * tcnud.getRemainQty()) - Math.ceil((mstmb.getCurPrice() * tcnud.getRemainQty()) * 0.001425) - Math.ceil((mstmb.getCurPrice() * tcnud.getRemainQty()) * 0.003)).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal unrealProfit = new BigDecimal(((mstmb.getCurPrice() * tcnud.getRemainQty()) - Math.ceil((mstmb.getCurPrice() * tcnud.getRemainQty()) * 0.001425) - Math.ceil((mstmb.getCurPrice() * tcnud.getRemainQty()) * 0.003)) - ((tcnud.getPrice() * tcnud.getQty()) + Math.ceil((tcnud.getPrice() * tcnud.getQty()) * 0.001425))).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal sumRemainQtyBigDecimal = new BigDecimal(sumRemainQty).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal sumFeeBigDecimal = new BigDecimal(sumFee).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal sumCostBigDecimal = new BigDecimal(sumCost).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal sumMarketValueBigDecimal = new BigDecimal(sumMarketValue).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal sumUnrealProfitBigDecimal = new BigDecimal(sumUnrealProfit).setScale(0, RoundingMode.HALF_UP);
                    FindStockResponse findStockResponse = new FindStockResponse();
                    findStockResponsePlusList.setStock(tcnud.getStock());
                    findStockResponsePlusList.setStockName(mstmb.getStockName());
                    findStockResponsePlusList.setNowPrice(nowPrice);
                    findStockResponsePlusList.setSumRemainQty(sumRemainQtyBigDecimal);
                    findStockResponsePlusList.setSumfee(sumFeeBigDecimal);
                    findStockResponsePlusList.setSumCost(sumCostBigDecimal);
                    findStockResponsePlusList.setSumMarketValue(sumMarketValueBigDecimal);
                    findStockResponsePlusList.setSumUnrealProfit(sumUnrealProfitBigDecimal);
                    findStockResponse.setTradeDate(tcnud.getTradeDate());
                    findStockResponse.setDocSeq(tcnud.getDocSeq());
                    findStockResponse.setStock(tcnud.getStock());
                    findStockResponse.setStockName(mstmb.getStockName());
                    findStockResponse.setBuyPrice(buyPrice);
                    findStockResponse.setNowPrice(nowPrice);
                    findStockResponse.setQty(qty);
                    findStockResponse.setRemainQty(remainQty);
                    findStockResponse.setFee(fee);
                    findStockResponse.setCost(cost);
                    findStockResponse.setMarketValue(marketValue);
                    findStockResponse.setUnrealProfit(unrealProfit);
                    findStockResponseResponses.add(findStockResponse);
                }
            }
        }
        findStockResponsePlusList.setDetailList(findStockResponseResponses);
        findStockResponsePlusLists.add(findStockResponsePlusList);
        List<FindStockResponsePlusList> entity = new ArrayList<>();
        //entity是將空白的資料轉為空的陣列
        if (request.getBranchNo() == null || request.getCustSeq() == null || (request.getBranchNo() == null && request.getCustSeq() == null)) {
            resultList.setResponseCode("002");
            resultList.setMessage("查無符合資料");
            resultList.setResultList(entity);
        }else if(tcnudRepository.findByBranchAndCustByAndStock(request.getBranchNo(),request.getCustSeq(),request.getStock()).isEmpty()){
            resultList.setResponseCode("001");
            resultList.setMessage("branchNo或CustSeq或stock資料輸入錯誤，請再次檢查");
            resultList.setResultList(entity);
        }
        for(Tcnud tcnud : tcnudList){
            if(request.getBranchNo().equals(tcnud.getBranchNo()) && request.getCustSeq().equals(tcnud.getCustSeq())){
                resultList.setResponseCode("000");
                resultList.setMessage("");
                resultList.setResultList(findStockResponsePlusLists);

            }
        }

        return resultList;
    }

    //------------------------------------------------------------------------------------------------------
    //第三題
    public SerachResult newStock(NewStockRequest newStockRequest) {
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");
        Hcmio newHcmio = new Hcmio();
        Tcnud newTcnud = new Tcnud();
        SerachResult resultList = new SerachResult();
        List<FindStockResponse> entity = new ArrayList<>();
        //給予不符合條件的選項空值
        String docseq = newStockRequest.getDocSeq();
        List<Tcnud> tcnudList;
        tcnudList = tcnudRepository.findByDocSeq(docseq);
        //用在判斷docseq的PK是否重複
        List<Mstmb> mstmbList = mstmbRepository.findAllMstmb();
        newHcmio.setTradeDate(newStockRequest.getTradeDate());
        newHcmio.setBranchNo(newStockRequest.getBranchNo());
        newHcmio.setCustSeq(newStockRequest.getCustSeq());
        newHcmio.setDocSeq(newStockRequest.getDocSeq());
        newHcmio.setStock(newStockRequest.getStock());
        newHcmio.setBsType("B");
        newHcmio.setPrice(newStockRequest.getPrice());
        newHcmio.setQty(newStockRequest.getQty());
        newHcmio.setAmt(newHcmio.getPrice() * newHcmio.getQty());
        newHcmio.setFee((newHcmio.getPrice() * newHcmio.getQty()) * 0.001425);
        newHcmio.setTax(0);
        newHcmio.setStinTax(0);
        newHcmio.setNetAmt(((newHcmio.getPrice() * newHcmio.getQty()) + ((newHcmio.getPrice() * newHcmio.getQty()) * 0.001425)) * -1);
        newHcmio.setModDate(date.format(LocalDate.now()));
        newHcmio.setModTime(time.format(LocalTime.now()));
        newHcmio.setModUser("Joe");

        newTcnud.setTradeDate(newStockRequest.getTradeDate());
        newTcnud.setBranchNo(newStockRequest.getBranchNo());
        newTcnud.setCustSeq(newStockRequest.getCustSeq());
        newTcnud.setDocSeq(newStockRequest.getDocSeq());
        newTcnud.setStock(newStockRequest.getStock());
        newTcnud.setPrice(newStockRequest.getPrice());
        newTcnud.setQty(newStockRequest.getQty());
        newTcnud.setRemainQty(newStockRequest.getQty());
        newTcnud.setFee(Math.ceil((newStockRequest.getPrice() * newStockRequest.getQty()) * 0.001425));
        newTcnud.setCost((newStockRequest.getPrice() * newStockRequest.getQty()) + (Math.ceil((newStockRequest.getPrice() * newStockRequest.getQty()) * 0.001425)));
        newTcnud.setModDate(date.format(LocalDate.now()));
        newTcnud.setModTime(time.format(LocalTime.now()));
        newTcnud.setModUser("Joe");

        List<FindStockResponse> FindStockResponseList = new ArrayList<>();
        NewStockResponse newStockResponses = new NewStockResponse();
        for (Mstmb mstmb : mstmbList) {
            if (newStockRequest.getStock().equals(mstmb.getStock())) {
                BigDecimal qty = new BigDecimal(newStockRequest.getQty()).setScale(0, RoundingMode.HALF_UP);
                BigDecimal nowPrice = new BigDecimal(mstmb.getCurPrice()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal buyPrice = new BigDecimal(newStockRequest.getPrice()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal remainQty = new BigDecimal(newStockRequest.getQty()).setScale(0, RoundingMode.HALF_UP);
                BigDecimal fee = new BigDecimal(Math.ceil((newHcmio.getPrice() * newHcmio.getQty()) * 0.001425)).setScale(0, RoundingMode.HALF_UP);
                BigDecimal cost = new BigDecimal((newStockRequest.getPrice() * newStockRequest.getQty()) + (Math.ceil((newStockRequest.getPrice() * newStockRequest.getQty()) * 0.001425))).setScale(0, RoundingMode.HALF_UP);
                BigDecimal marketValue = new BigDecimal((mstmb.getCurPrice() * newStockRequest.getQty()) - Math.ceil((mstmb.getCurPrice() * newStockRequest.getQty()) * 0.001425) - Math.ceil((mstmb.getCurPrice() * newStockRequest.getQty()) * 0.003)).setScale(0, RoundingMode.HALF_UP);
                BigDecimal unrealProfit = new BigDecimal(((mstmb.getCurPrice() * newStockRequest.getQty()) - Math.ceil((mstmb.getCurPrice() * newStockRequest.getQty()) * 0.001425) - Math.ceil((mstmb.getCurPrice() * newStockRequest.getQty()) * 0.003)) - ((newStockRequest.getPrice() * newStockRequest.getQty()) + Math.ceil((newStockRequest.getPrice() * newStockRequest.getQty()) * 0.001425))).setScale(0, RoundingMode.HALF_UP);
                newStockResponses.setTradeDate(newStockRequest.getTradeDate());
                newStockResponses.setDocSeq(newStockRequest.getDocSeq());
                newStockResponses.setStock(newStockRequest.getStock());
                newStockResponses.setStockName(mstmb.getStockName());
                newStockResponses.setBuyPrice(buyPrice);
                newStockResponses.setNowPrice(nowPrice);
                newStockResponses.setQty(qty);
                newStockResponses.setRemainQty(remainQty);
                newStockResponses.setFee(fee);
                newStockResponses.setCost(cost);
                newStockResponses.setMarketValue(marketValue);
                newStockResponses.setUnrealProfit(unrealProfit);
                FindStockResponseList.add(newStockResponses);
            }
        }

        if(newStockRequest.getStock().isBlank() || newStockRequest.getStock()==null ){
            resultList.setResponseCode("002");
            resultList.setMessage("stock的資料輸入錯誤，請再次檢查");
            resultList.setResultList(entity);
        }else if(newStockRequest.getQty().isNaN() || newStockRequest.getQty() ==null || newStockRequest.getQty()<0) {
            resultList.setResponseCode("002");
            resultList.setMessage("qty的資料輸入錯誤，請再次檢查");
            resultList.setResultList(entity);
        }else if (newStockRequest.getTradeDate().isBlank() || newStockRequest.getTradeDate()==null) {
            resultList.setResponseCode("002");
            resultList.setMessage("tradedate的資料輸入錯誤，請再次檢查");
            resultList.setResultList(entity);
        }else if (newStockRequest.getBranchNo().isBlank() || newStockRequest.getBranchNo()==null) {
            resultList.setResponseCode("002");
            resultList.setMessage("branchNo的資料輸入錯誤，請再次檢查");
            resultList.setResultList(entity);
        }else if (newStockRequest.getCustSeq().isBlank()||newStockRequest.getCustSeq()==null){
            resultList.setResponseCode("002");
            resultList.setMessage("custseq的資料輸入錯誤，請再次檢查");
            resultList.setResultList(entity);
        }else if(newStockRequest.getDocSeq().isBlank() || newStockRequest.getDocSeq()==null){
            resultList.setResponseCode("002");
            resultList.setMessage("docseq的資料輸入錯誤，請再次檢查");
            resultList.setResultList(entity);
        } else if (newStockRequest.getPrice().isNaN() || newStockRequest.getPrice() ==null || newStockRequest.getPrice()>1000 || newStockRequest.getPrice()<0) {
            resultList.setResponseCode("002");
            resultList.setMessage("price的資料輸入錯誤，請再次檢查");
            resultList.setResultList(entity);
        }else{
            resultList.setResponseCode("000");
            resultList.setMessage("");
            resultList.setResultList(FindStockResponseList);

        }
            for(Tcnud tcnud : tcnudList){
                if(tcnud.getDocSeq().equals(newStockRequest.getDocSeq())) {
                    resultList.setResponseCode("002");
                    resultList.setMessage("docseq的資料重複，請更換");
                    resultList.setResultList(entity);
                }
            }
            //由於foreach無法寫入上面的判斷式，就放到最後去判斷，如果符合條件就把上面的判斷結果覆蓋
            if(resultList.getResponseCode().equals("000")){
                hcmioRepository.save(newHcmio);
                tcnudRepository.save(newTcnud);
            }
            //用字串判斷新增條件是否符合資格，若符合則儲存資料到資料庫

        return resultList;
    }
    //-------------------------------------------------------------------------------------------------
    //更新mstmb現價
    public UpdateResult updateMstmb(UpdataMstmbNowPrice request) {
        Mstmb mstmb = new Mstmb();
        List<Mstmb> mstmbList;
        String verify = new String();
        UpdateResult updateResult = new UpdateResult();
        List<BigDecimal> entity = new ArrayList<>();
        //一個空的陣列用來顯示錯誤的nowprice
        mstmbList = mstmbRepository.findByStock(request.getStock());
        for(Mstmb mstmb1 : mstmbList) {
            if (request.getStock().equals(mstmb1.getStock())) {
                List<BigDecimal> nowPriceToBigDecimal = new ArrayList<>();
                BigDecimal nowPrice = new BigDecimal(request.getCurPrice()).setScale(2, RoundingMode.HALF_UP);
                mstmb.setCurPrice(request.getCurPrice());
                mstmb.setStock(request.getStock());
                mstmb.setMarketType(mstmb1.getMarketType());
                mstmb.setStockName(mstmb1.getStockName());
                mstmb.setRefPrice(request.getCurPrice());
                mstmb.setCurrency(mstmb1.getCurrency());
                mstmb.setModDate(mstmb1.getModDate());
                mstmb.setModTime(mstmb1.getModTime());
                mstmb.setModUser(mstmb1.getModUser());
                mstmbRepository.save(mstmb);
                nowPriceToBigDecimal.add(nowPrice);
                updateResult.setNowPrice(nowPriceToBigDecimal);
                verify = "success";
                }
            }
        if(verify.equals("success")){
            updateResult.setResponseCode("000");
            updateResult.setMessage("");
        }else if(request.getStock().equals(null) || request.getStock().equals("")){
            updateResult.setNowPrice(entity);
            updateResult.setResponseCode("002");
            updateResult.setMessage("參數檢核錯誤");
        }else{
            updateResult.setNowPrice(entity);
            updateResult.setResponseCode("001");
            updateResult.setMessage("stock輸入錯誤");
        }

            return updateResult;
    }

    //-------------------------------------------------------------------------------------------------
    //包裝方法

    private Double getAmt(Double price, Double qty) {
        //價金
        return price * qty;
    }

    private Integer getFee(Double amt) {
        //手續費
        return (int) Math.round(amt * 0.001425);
    }


    //-----------------------------------------------------------------------------
    //查詢獲利區間
    public ProfitMargin getStockData1(FindProfitMarginRequest request) {
        String branchNo = request.getBranchNo();
        String custSeq = request.getCustSeq();
        String stock = request.getStock();
        List<Mstmb> mstmbList;
        List<Tcnud> tcnudList;
        if (null == stock) {
            tcnudList = tcnudRepository.findByBranchAndCustSeq(branchNo, custSeq);
            mstmbList = mstmbRepository.findAll();
        } else {
            mstmbList = mstmbRepository.findByStock(stock);
            tcnudList = tcnudRepository.findByBranchAndCustByAndStock(branchNo, custSeq, stock);
        }
        //判斷stock是否為空值
        List<FindProfitMarginResponse> findProfitMarginResponses = new ArrayList<>();
        ProfitMargin resultList = new ProfitMargin();
        for (Mstmb mstmb : mstmbList) {
            for (Tcnud tcnud : tcnudList) {
                if (mstmb.getStock().equals(tcnud.getStock())) {
                    BigDecimal buyPrice = new BigDecimal(tcnud.getPrice()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal nowPrice = new BigDecimal(mstmb.getCurPrice()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal qty = new BigDecimal(tcnud.getQty()).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal remainQty = new BigDecimal(tcnud.getRemainQty()).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal fee = new BigDecimal(tcnud.getFee()).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal cost = new BigDecimal(Math.ceil((tcnud.getPrice() * tcnud.getQty()) + ((tcnud.getPrice() * tcnud.getQty()) * 0.001425))).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal marketValue = new BigDecimal((mstmb.getCurPrice() * tcnud.getRemainQty()) - Math.ceil((mstmb.getCurPrice() * tcnud.getRemainQty()) * 0.001425) - Math.ceil((mstmb.getCurPrice() * tcnud.getRemainQty()) * 0.003)).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal unrealProfit = new BigDecimal(((mstmb.getCurPrice() * tcnud.getRemainQty()) - Math.ceil((mstmb.getCurPrice() * tcnud.getRemainQty()) * 0.001425) - Math.ceil((mstmb.getCurPrice() * tcnud.getRemainQty()) * 0.003)) - ((tcnud.getPrice() * tcnud.getQty()) + Math.ceil((tcnud.getPrice() * tcnud.getQty()) * 0.001425))).setScale(0, RoundingMode.HALF_UP);
                    double unrealProfitDouble = unrealProfit.doubleValue();
                    double costDouble = cost.doubleValue();
                    BigDecimal profitMargin = new BigDecimal(((unrealProfitDouble/costDouble)*100)).setScale(2, RoundingMode.HALF_UP);
                    FindProfitMarginResponse findProfitMarginResponse = new FindProfitMarginResponse();
                    findProfitMarginResponse.setTradeDate(tcnud.getTradeDate());
                    findProfitMarginResponse.setDocSeq(tcnud.getDocSeq());
                    findProfitMarginResponse.setStock(tcnud.getStock());
                    findProfitMarginResponse.setStockName(mstmb.getStockName());
                    findProfitMarginResponse.setBuyPrice(buyPrice);
                    findProfitMarginResponse.setNowPrice(nowPrice);
                    findProfitMarginResponse.setQty(qty);
                    findProfitMarginResponse.setRemainQty(remainQty);
                    findProfitMarginResponse.setFee(fee);
                    findProfitMarginResponse.setCost(cost);
                    findProfitMarginResponse.setMarketValue(marketValue);
                    findProfitMarginResponse.setUnrealProfit(unrealProfit);
                    findProfitMarginResponse.setProfitMargin(profitMargin.toString()+"%");
                    double profitMarginDouble = profitMargin.doubleValue();
                    if(profitMarginDouble>request.getLowLimit() && profitMarginDouble< request.getTopLimit()){
                        findProfitMarginResponses.add(findProfitMarginResponse);
                    }
                }
            }
        }


        if (request.getBranchNo() == null || request.getCustSeq() == null || (request.getBranchNo() == null && request.getCustSeq() == null)) {
            resultList.setResponseCode("002");
            resultList.setMessage("查無符合資料");
        } else if (tcnudRepository.findByBranchAndCustByAndStock(request.getBranchNo(), request.getCustSeq(), request.getStock()).isEmpty()) {
            resultList.setResponseCode("001");
            resultList.setMessage("branchNo或CustSeq或stock資料輸入錯誤，請再次檢查");
        }
        for (Tcnud tcnud : tcnudList) {
            if (request.getBranchNo().equals(tcnud.getBranchNo()) && request.getCustSeq().equals(tcnud.getCustSeq())) {
                resultList.setResponseCode("000");
                resultList.setMessage("");
            }
        }

        resultList.setResultList(findProfitMarginResponses);
        if(resultList.getResultList().isEmpty()){
            resultList.setResponseCode("000");
            resultList.setMessage("查詢成功 但區間內沒有資料");
        }
        return resultList;
    }
}
