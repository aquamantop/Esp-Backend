package com.top.apiwallet.services;

import com.top.apiwallet.client.CustomerService;
import com.top.apiwallet.exceptions.WalletExceptions;
import com.top.apiwallet.exceptions.MessageError;
import com.top.apiwallet.model.Currency;
import com.top.apiwallet.model.CustomerDTO;
import com.top.apiwallet.model.Wallet;
import com.top.apiwallet.repository.WalletRepository;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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

    @CircuitBreaker(name="customer",fallbackMethod="getCustomerFallbackValue")
    @Retry(name = "customer")
    public Wallet add(Wallet wallet) throws WalletExceptions {
        if(customerService.getCustomer(wallet.getDocType(), wallet.getDocNum()).isPresent()){
            if(walletRepository.findByDocTypeAndDocNumAndCurrency_Code(wallet.getDocType(), wallet.getDocNum(), wallet.getCurrency().getCode()).isPresent()){
                throw new WalletExceptions(MessageError.WALLET_EXISTS);
            } else return walletRepository.save(wallet);
        } else throw new WalletExceptions(MessageError.CUSTOMER_NOT_FOUND);
    }

    private Wallet getCustomerFallbackValue(CallNotPermittedException ex) {
        return new Wallet(666L, "dniTypeError", "dniError",
                new Currency("codigoError", "descripcionError"), 0.0);
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
