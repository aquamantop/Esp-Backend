package com.top.apiwallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Customer not found")
public class WalletExceptions extends Exception{

    public WalletExceptions(MessageError message) {
        super(message.message);
    }

}