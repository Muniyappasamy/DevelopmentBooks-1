package com.bnpp.fortis.developmentbooks.exception;

import com.bnpp.fortis.developmentbooks.model.BookApiException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler extends Exception {


    private static final String INVALID_BOOK_ERROR_CODE = "BOOK_00000";
    private static final String INVALID_BOOK_QUANTITY_ERROR_CODE = "BOOK_00001";

    private static final String INVALID_JSON__ERROR_CODE = "BOOK_0002";
    private static final String INVALID_JSON__ERROR_DESCRIPTION = "Please check the Given Input Is Not Valid Either Book Name is wrong Or Quantity is Non-Numeric";



    @ExceptionHandler({InvalidBookException.class})
    public ResponseEntity<Object> handleBookTitleBadRequestException(InvalidBookException ex) {
        BookApiException bookApiException = new BookApiException(INVALID_BOOK_ERROR_CODE, ex.getMessage());
        return new ResponseEntity<Object>(bookApiException, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleJsonMappingException(JsonMappingException ex) {
        BookApiException bookApiException = new BookApiException(INVALID_BOOK_ERROR_CODE,INVALID_JSON__ERROR_DESCRIPTION);
        return new ResponseEntity<Object>(bookApiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidQuantityException.class})
    public ResponseEntity<Object> handleBookQuantityBadRequestException(InvalidQuantityException ex) {
        BookApiException bookApiException = new BookApiException(INVALID_BOOK_QUANTITY_ERROR_CODE, ex.getMessage());
        return new ResponseEntity<Object>(bookApiException, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
