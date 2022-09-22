package com.example.connectdb.model;

import com.example.connectdb.controller.dto.request.NewStockRequest;
import com.example.connectdb.controller.dto.response.FindStockResponse;
import com.example.connectdb.controller.dto.response.NewStockResponse;
import com.example.connectdb.model.entity.hcmio.Hcmio;
import com.example.connectdb.model.entity.hcmio.HcmioKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface HcmioRepository extends JpaRepository<Hcmio, HcmioKey> {
    Hcmio findByDocSeq(String docSeq);

    @Query(value = "SELECT  *\n" +
            "FROM hcmio\n" +
            "WHERE branch_No = ?1 AND cust_Seq = ?2 AND stock = ?3",nativeQuery = true)
    List<Hcmio>  findByNoAndCust(String branchNo, String custSeq, String stock);


}
