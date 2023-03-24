package com.top.apicard.repository;

import com.top.apicard.model.MovimientosTarjetaCredito;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovimientosTarjetaCreditoRepository extends MongoRepository<MovimientosTarjetaCredito, String> {
}
