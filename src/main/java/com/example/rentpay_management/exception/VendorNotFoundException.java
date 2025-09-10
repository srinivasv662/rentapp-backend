package com.example.rentpay_management.exception;

public class VendorNotFoundException extends RuntimeException{

    public VendorNotFoundException(String msg) {
        super(String.format(msg));
    }

}
