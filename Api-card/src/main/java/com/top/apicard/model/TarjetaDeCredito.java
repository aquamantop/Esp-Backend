package com.top.apicard.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Document(collection = "Tarjeta De Credito")
public class TarjetaDeCredito {
    @Id
    private String numeroDeTarjeta;

    private String tipoDocumento;

    private String numeroDocumento;

    private String moneda;

    private BigDecimal limiteCalificado;

    private BigDecimal limiteConsumido;

    private BigDecimal limiteDisponible;


}
