package com.example.rentpay_management.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String path;

    public ErrorMessage(int statusCode, Date timestamp, String message, String path) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.path = path;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

}