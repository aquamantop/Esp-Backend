package com.top.apiwallet.repository;

import com.top.apiwallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByDocTypeAndDocNumAndCurrency_Code(String docType, String docNum, String code);

    List<Wallet> findByDocTypeAndDocNum(String docType, String docNum);

}

