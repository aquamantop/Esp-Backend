package com.top.apicard.controller;

import com.top.apicard.model.MovimientosTarjetaCredito;
import com.top.apicard.model.TarjetaDeCredito;
import com.top.apicard.services.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tarjeta")
public class TarjetaCreditoController {

    private final CardService cardService;

    public TarjetaCreditoController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/{documentType}/{documentNumber}/{currency}")
    public ResponseEntity<TarjetaDeCredito> crearTarjeta(@PathVariable String documentType, @PathVariable String documentNumber, @PathVariable String currency){
        ResponseEntity response = null;

        if(documentNumber != null && documentType != null && currency != null){
            response = new ResponseEntity(cardService.createCreditCard(documentType, documentNumber, currency), HttpStatus.OK);
        } else response = new ResponseEntity("Atributos nulos", HttpStatus.FORBIDDEN);

        return response;
    }

    @GetMapping("/{documentType}/{documentNumber}")
    public ResponseEntity<TarjetaDeCredito> obtenerPorTipoYNumeroDocumento(@PathVariable String documentType, @PathVariable String documentNumber){
        ResponseEntity response = null;

        if(documentType != null && documentNumber != null){
            response = new ResponseEntity(cardService.getByDocTypeAndDocNumber(documentType, documentNumber), HttpStatus.OK);
        } else response = new ResponseEntity("Atributos nulos", HttpStatus.FORBIDDEN);

        return response;
    }

    @GetMapping()
    public ResponseEntity<List<TarjetaDeCredito>> listaTarjetas(){
        return new ResponseEntity<>(cardService.getAllCards(), HttpStatus.OK);
    }

    @PostMapping("/debitar")
    public void debitar(@RequestBody MovimientosTarjetaCredito movimientosTarjetaCredito){
        /* POST (debitar, se debe pasar todos los datos de movimiento, e
        internamente hacer el débito) */
        cardService.debit(movimientosTarjetaCredito);
    }

    @PostMapping("/pagar/{cardNumber}/{documentType}/{documentNumber}")
    public void pagarTarjeta(@PathVariable String cardNumber, @PathVariable String documentType, @PathVariable String documentNumber){

        /*POST(pagar tarjeta, se pasa Numero de tarjeta, Tipo y Número de
                documento):
        1) Se consulta api-wallet si tiene disponible para pagar
        2) Se debita el dinero de api-wallet
        3) Se devuelve el límite disponible
        4) En caso de no haber disponible se lanza un error.*/
    }

}
