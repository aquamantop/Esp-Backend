package com.top.apicard.repository;

import com.top.apicard.model.TarjetaDeCredito;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TarjetaCreditoRepository extends MongoRepository<TarjetaDeCredito, String> {

    TarjetaDeCredito findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);

    TarjetaDeCredito findByNumeroDeTarjeta(String numeroDeTarjeta);

}
