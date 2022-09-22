package com.example.connectdb.model.entity.hcmio;


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
@Table(name = "hcmio")
@IdClass(HcmioKey.class)
public class Hcmio {
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
    private String bsType;
    @Column
    private double price;
    @Column
    private double qty;
    @Column
    private double amt;
    @Column
    private double fee;
    @Column
    private double tax;
    @Column
    private double stinTax;
    @Column
    private double netAmt;
    @Column
    private String modDate;
    @Column
    private String modTime;
    @Column
    private String modUser;
}
