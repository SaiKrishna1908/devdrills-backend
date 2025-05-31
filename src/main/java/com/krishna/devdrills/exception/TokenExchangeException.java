package com.krishna.devdrills.exception;

import lombok.Data;

@Data
public class TokenExchangeException extends Exception {

    private String message;
    private int code;

    public TokenExchangeException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }
}
