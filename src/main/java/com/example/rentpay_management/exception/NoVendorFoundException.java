package com.example.rentpay_management.exception;

public class NoVendorFoundException extends RuntimeException{

    public NoVendorFoundException(String msg) {
        super(String.format(msg));
    }

}
