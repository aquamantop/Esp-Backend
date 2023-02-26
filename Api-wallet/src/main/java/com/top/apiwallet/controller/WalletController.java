package com.top.apiwallet.controller;

import com.top.apiwallet.model.Wallet;
import com.top.apiwallet.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/{docType}/{docNum}/{code}")
    public ResponseEntity<Optional<Wallet>> getWallet (@PathVariable String docType,
                                       @PathVariable String docNum,
                                       @PathVariable String code){
        ResponseEntity response = null;

        if(docType != null && docNum != null && code != null){
            response = new ResponseEntity(walletService.getWalletByCurrency(docType, docNum, code), HttpStatus.OK);
        } else response = new ResponseEntity("Missing parameters", HttpStatus.FORBIDDEN);

        return response;
    }

    @PostMapping("/save")
    public ResponseEntity<Wallet> save (@RequestBody Wallet wallet){
        ResponseEntity response = null;

        if(wallet != null){
            response = new ResponseEntity(walletService.save(wallet), HttpStatus.OK);
        } else response = new ResponseEntity("Wallet is null", HttpStatus.FORBIDDEN);

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
