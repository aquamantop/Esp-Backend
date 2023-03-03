package com.top.apiwallet.controller;

import com.top.apiwallet.exceptions.CustomerNotFoundException;
import com.top.apiwallet.model.Wallet;
import com.top.apiwallet.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/{docType}/{docNum}/{code}")
    public ResponseEntity<Optional<Wallet>> get (@PathVariable String docType,
                                       @PathVariable String docNum,
                                       @PathVariable String code){
        ResponseEntity response = null;

        if(docType != null && docNum != null && code != null){
            response = new ResponseEntity(walletService.getWalletByCurrency(docType, docNum, code), HttpStatus.OK);
        } else response = new ResponseEntity("Missing parameters", HttpStatus.FORBIDDEN);

        return response;
    }

    @PostMapping("/add")
    public ResponseEntity<Wallet> add (@RequestBody Wallet wallet) throws CustomerNotFoundException {
        ResponseEntity response = null;

        List<Wallet> lista = walletService.getBalanceByDocument(wallet.docType, wallet.docNum);

        Integer n = 0;

        for (Wallet w : lista) {
            if(w.getCurrency().getId() == wallet.getCurrency().getId()){
                n++;
            }
        }
        if(n == 0){
            response = new ResponseEntity<>(walletService.add(wallet), HttpStatus.OK);
        } else response = new ResponseEntity("This currency is already in the wallet", HttpStatus.FORBIDDEN);


        return response;

    }

    @GetMapping("/{docType}/{docNum}")
    public ResponseEntity<List<Wallet>> getBalance (@PathVariable String docType,
                                     @PathVariable String docNum){
        ResponseEntity response = null;

        if(docType != null && docNum != null){
            response = new ResponseEntity(walletService.getBalanceByDocument(docType, docNum), HttpStatus.OK);
        } else response = new ResponseEntity("Missing parameters", HttpStatus.FORBIDDEN);

        return response;
    }


    @PutMapping("/update")
    public ResponseEntity<Wallet> updateWallet (@RequestBody Wallet wallet){
        ResponseEntity response = null;

        if(wallet != null){
            if(wallet.getCurrency() != null || wallet.getDocNum() != null || wallet.getDocType() != null){
                response = new ResponseEntity("these data cannot be modified", HttpStatus.FORBIDDEN);
            } else response = new ResponseEntity(walletService.update(wallet), HttpStatus.OK);
        } else response = new ResponseEntity("Wallet is null", HttpStatus.FORBIDDEN);

        return response;
    }
}
