package com.example.connectdb.model;

import com.example.connectdb.model.entity.tcnud.Tcnud;
import com.example.connectdb.model.entity.tcnud.TcnudKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TcnudRepository extends JpaRepository<Tcnud, TcnudKey> {
        Tcnud findByCustSeq(String custSeq);
        @Query(value = "SELECT  *\n" +
                "FROM tcnud\n" +
                "WHERE branch_No = ?1 AND cust_Seq = ?2 AND stock = ?3",nativeQuery = true)
        List<Tcnud> findByBranchAndCustByAndStock(String branchNo, String custSeq, String stock);
        @Query(value = "SELECT  *\n" +
                "FROM tcnud\n" +
                "WHERE branch_No = ?1 AND cust_Seq = ?2",nativeQuery = true)
        List<Tcnud> findByBranchAndCustSeq(String branchNo, String custSeq);
        @Query(value = "SELECT  *\n" +
                "FROM tcnud\n" +
                "WHERE doc_seq = ?1",nativeQuery = true)
        List<Tcnud> findByDocSeq(String docseq);


}
