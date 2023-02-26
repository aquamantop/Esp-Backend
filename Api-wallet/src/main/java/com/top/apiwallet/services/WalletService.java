package com.top.apiwallet.services;

import com.top.apiwallet.model.Wallet;
import com.top.apiwallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    public Wallet update(Wallet wallet) {
        Optional<Wallet> w = walletRepository.findById(wallet.getId());

        if(wallet.getBalance() != null){
            w.get().setBalance(wallet.getBalance());
        }

        return walletRepository.save(w.get());
    }

    public Optional<Wallet> getWalletByCurrency(String documentType, String document, String code) {
        return walletRepository.findByDocTypeAndDocNumAndCurrency_Code(documentType, document, code);
    }

    public List<Wallet> getBalanceByDocument(String documentType, String document) {
        return walletRepository.findByDocTypeAndDocNum(documentType, document);
    }
}
