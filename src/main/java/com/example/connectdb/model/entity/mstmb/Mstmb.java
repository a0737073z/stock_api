package com.example.connectdb.model.entity.mstmb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mstmb")
public class Mstmb {
    @Id
    @Column
    private String stock;
    @Column
    private String stockName;
    @Column
    private String marketType;
    @Column
    private double curPrice;
    @Column
    private double refPrice;
    @Column
    private String currency;
    @Column
    private String modDate;
    @Column
    private String modTime;
    @Column
    private String modUser;
}
