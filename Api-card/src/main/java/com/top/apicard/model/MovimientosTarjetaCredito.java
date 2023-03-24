package com.top.apicard.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@Document(collection = "Movimientos Tarjeta de Credito")
public class MovimientosTarjetaCredito {
    @Id
    private String id;
    private String numeroDeTarjeta;
    private TipoMovimiento tipoMovimiento;
    private Importe importe;
    private LocalDateTime fechaOperacion;
    private Cobrador cobrador;
    private String descripcion;
    private List<DetalleCompra> detalleCompra;
    private Estado estado;
    private BigDecimal comisionBilletera;


    @Data
    @Builder
    public static class Importe{
        private String tipoMoneda;
        private BigDecimal valor;
    }

    @Data
    @Builder
    public static class Cobrador{
        private String tipoDocumento;
        private String numeroDocumento;
        private String razonSocial;
    }

    @Data
    @Builder
    public static class DetalleCompra{
        private String articulo;
        private Integer cantidad;
        private BigDecimal importeUnitario;
        private BigDecimal importeSubtotal;
    }

    public enum TipoMovimiento {
        DEBITO, CREDITO;
    }

    public enum Estado {
        ACTIVO, ANULADO;
    }


}



