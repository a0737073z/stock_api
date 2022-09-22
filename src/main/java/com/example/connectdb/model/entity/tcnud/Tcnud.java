package com.example.connectdb.model.entity.tcnud;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tcnud")
@IdClass(TcnudKey.class)
public class Tcnud {
    @Id
    @Column
    private String tradeDate;
    @Id
    @Column
    private String branchNo;
    @Id
    @Column
    private String custSeq;
    @Id
    @Column
    private String docSeq;
    @Column
    private String stock;
    @Column
    private double price;
    @Column
    private double qty;
    @Column
    private  double remainQty;
    @Column
    private double fee;
    @Column
    private double cost;
    @Column
    private  String modDate;
    @Column
    private String modTime;
    @Column
    private  String modUser;
}
