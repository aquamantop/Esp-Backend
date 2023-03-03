package com.top.apiwallet.services;

import com.top.apiwallet.client.CustomerService;
import com.top.apiwallet.exceptions.CustomerNotFoundException;
import com.top.apiwallet.model.CustomerDTO;
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

    @Autowired
    private CustomerService customerService;

    public Optional<CustomerDTO> getCustomer(String docType, String docNum){
        return customerService.getCustomer(docType, docNum);
    }

    public Wallet add(Wallet wallet) throws CustomerNotFoundException {
        if(getCustomer(wallet.docType, wallet.docNum).isPresent()){
            return walletRepository.save(wallet);
        } else throw new CustomerNotFoundException("Customer not found");
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
