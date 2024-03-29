package com.top.apiwallet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String docType;

    public String docNum;

    @ManyToOne
    @JoinColumn(name = "idCurrency", nullable = false)
    public Currency currency;

    public Double balance;

    public Wallet(String docType, String docNum, Currency currency, Double balance) {
        this.docType = docType;
        this.docNum = docNum;
        this.currency = currency;
        this.balance = balance;
    }
}


