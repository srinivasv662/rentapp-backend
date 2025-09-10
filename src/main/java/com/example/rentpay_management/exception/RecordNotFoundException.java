package com.example.rentpay_management.exception;

public class RecordNotFoundException extends RuntimeException{

    public RecordNotFoundException(String msg) {
        super(String.format(msg));
    }

}
