package com.top.apicard.services;

import com.top.apicard.client.IMarginsService;
import com.top.apicard.client.IWalletService;
import com.top.apicard.model.MovimientosTarjetaCredito;
import com.top.apicard.model.TarjetaDeCredito;
import com.top.apicard.repository.TarjetaCreditoRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class CardService {

    private final TarjetaCreditoRepository tarjetaCreditoRepository;
    private final IMarginsService iMarginsService;
    private final IWalletService iWalletService;

    public CardService(TarjetaCreditoRepository tarjetaCreditoRepository, IMarginsService iMarginsService, IWalletService iWalletService) {
        this.tarjetaCreditoRepository = tarjetaCreditoRepository;
        this.iMarginsService = iMarginsService;
        this.iWalletService = iWalletService;
    }

    public TarjetaDeCredito createCreditCard(String docType, String docNumber, String currency) {
        var margins = iMarginsService.getMargins(docType, docNumber);

        return tarjetaCreditoRepository.save(
                TarjetaDeCredito.builder()
                        .tipoDocumento(docType)
                        .numeroDocumento(docNumber)
                        .numeroDeTarjeta(generarNumeroAleatorio16Cifras())
                        .moneda(currency)
                        .limiteCalificado(margins.get().getTotalMargin())
                        .limiteConsumido(new BigDecimal(0))
                        .limiteDisponible(margins.get().getTotalMargin())
                        .build()
        );
    }

    // Generamos un número aleatorio de 16 cifras para la tarjeta.
    public static String generarNumeroAleatorio16Cifras() {
        Random random = new Random();
        String numero = "";
        for (int i = 0; i < 16; i++) {
            numero = numero + random.nextInt(10);
        }
        return numero;
    }

    public TarjetaDeCredito getByDocTypeAndDocNumber(String docType, String docNumber) {
        return tarjetaCreditoRepository.findByTipoDocumentoAndNumeroDocumento(docType, docNumber);
    }

    public List<TarjetaDeCredito> getAllCards() {
        return tarjetaCreditoRepository.findAll();
    }

    public void debit(MovimientosTarjetaCredito movimientosTarjetaCredito) {
        TarjetaDeCredito tarjeta = tarjetaCreditoRepository.findByNumeroDeTarjeta(movimientosTarjetaCredito.getNumeroDeTarjeta());
        BigDecimal importeTotal = movimientosTarjetaCredito.getImporte().getValor();

        // Verificamos que la tarjeta exista y tenga limite disponible mayor o igual al importe total a debitar
        if(!tarjeta.getNumeroDeTarjeta().isEmpty() && tarjeta.getLimiteDisponible().compareTo(importeTotal) >= 0){
            // Actualizamos el limite consumido(+) y el limite disponible(-)
            tarjetaCreditoRepository.save(
                    TarjetaDeCredito.builder()
                            .tipoDocumento(tarjeta.getTipoDocumento())
                            .numeroDocumento(tarjeta.getNumeroDocumento())
                            .numeroDeTarjeta(tarjeta.getNumeroDeTarjeta())
                            .moneda(tarjeta.getMoneda())
                            .limiteCalificado(tarjeta.getLimiteCalificado())
                            .limiteConsumido(tarjeta.getLimiteConsumido().add(importeTotal))
                            .limiteDisponible(tarjeta.getLimiteDisponible().subtract(importeTotal))
                            .build()
            );
        } else {
            // Acá deberiamos lanzar un error
            System.out.println("El importe supera el limite disponible");
        }

    }

    public void payCreditCard(String cardNumber, String docType, String docNumber) {
        var wallet = iWalletService.getWallet(docType, docNumber);
        double balanceWallet = wallet.get().getBalance();

        var tarjeta = this.getByDocTypeAndDocNumber(docType, docNumber);

        // Verificamos que el numero de tarjeta pasado por parametro sea igual al numero de tarjeta buscado por num documento
        // Verificamos que el limite consumido sea menor o igual al balance del wallet (disponible para pagar)
        if(Objects.equals(tarjeta.getNumeroDeTarjeta(), cardNumber) &&
                tarjeta.getLimiteConsumido().compareTo(BigDecimal.valueOf(balanceWallet)) <= 0){
            // Hay que debitar el dinero de la wallet
            // Hay que devolver el limite disponible
        }

        // Si no hay dinero disponible lanzar error

    }
}
