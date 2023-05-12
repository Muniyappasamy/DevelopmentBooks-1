package com.bnpp.fortis.developmentbooks.exception;

import com.bnpp.fortis.developmentbooks.model.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler extends Exception {


    private static final String INVALID_BOOK_ERROR_CODE = "BOOK_00000";
    private static final String INVALID_BOOK_QUANTITY_ERROR_CODE = "BOOK_00001";

    @ExceptionHandler({InvalidBookException.class})
    public ResponseEntity<Object> handleBookTitleBadRequestException(InvalidBookException ex, WebRequest request) {
        ApiError apiError = new ApiError(INVALID_BOOK_ERROR_CODE, ex.getMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidQuantityException.class})
    public ResponseEntity<Object> handleBookQuantityBadRequestException(InvalidQuantityException ex,WebRequest request) {
        ApiError apiError = new ApiError(INVALID_BOOK_QUANTITY_ERROR_CODE, ex.getMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
