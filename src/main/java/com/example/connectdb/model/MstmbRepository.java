package com.example.connectdb.model;

import com.example.connectdb.model.entity.mstmb.Mstmb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MstmbRepository extends JpaRepository<Mstmb,Integer> {
        @Query(value = "SELECT  *\n" +
                "FROM mstmb\n" +
                "WHERE stock = ?1",nativeQuery = true)
        List<Mstmb> findByStock(String stock);

        @Query(value = "SELECT * FROM mstmb",nativeQuery = true)
        List<Mstmb> findAllMstmb();

}
