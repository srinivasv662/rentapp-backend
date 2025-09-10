package com.example.rentpay_management.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);


    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> RecordNotFoundHandler(Exception ex, WebRequest request) {
        logger.error("*********BEGIN**************RecordNotFoundException Exception******************BEGIN*******************\n");
        logger.error(ex.fillInStackTrace());
        logger.error("*********ENDS**************RecordNotFoundException Exception*******************ENDS*****************************\n");
        log.warn("RecordNotFoundHandler for "+HttpStatus.NOT_FOUND.value());
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoVendorFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> NoVendorFoundHandler(Exception ex, WebRequest request) {
        logger.error("*********BEGIN**************NoVendorFoundException Exception******************BEGIN*******************\n");
        logger.error(ex.fillInStackTrace());
        logger.error("*********ENDS**************NoVendorFoundException Exception*******************ENDS*****************************\n");
        log.warn("NoVendorFoundException for "+HttpStatus.NOT_FOUND.value());
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                "No Vendor Found",
                request.getDescription(false));
        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VendorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> VendorNotFoundHandler(Exception ex, WebRequest request) {
        logger.error("*********BEGIN**************VendorNotFoundException Exception******************BEGIN*******************\n");
        logger.error(ex.fillInStackTrace());
        logger.error("*********ENDS**************VendorNotFoundException Exception*******************ENDS*****************************\n");
        log.warn("VendorNotFoundHandler for "+HttpStatus.NOT_FOUND.value());
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SignatureIncorrectException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorMessage> SignatureIncorrectExceptionHandler(Exception ex, WebRequest request) {
        logger.error("*********BEGIN**************SignatureIncorrectException ******************BEGIN*******************\n");
        logger.error(ex.fillInStackTrace());
        logger.error("*********ENDS**************SignatureIncorrectException *******************ENDS*****************************\n");
        log.warn("SignatureIncorrectException for "+HttpStatus.NOT_FOUND.value());
        ErrorMessage message = new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<ErrorMessage>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Something went wrong", "details", ex.getMessage()));
    }

}
