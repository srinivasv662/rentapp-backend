package com.example.rentpay_management.exception;

public class SignatureIncorrectException extends RuntimeException{

    public SignatureIncorrectException(String msg) {
        super(String.format(msg));
    }

}
