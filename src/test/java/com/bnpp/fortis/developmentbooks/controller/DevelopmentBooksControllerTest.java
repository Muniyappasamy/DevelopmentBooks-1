package com.bnpp.fortis.developmentbooks.controller;

import com.bnpp.fortis.developmentbooks.model.BookDto;
import com.bnpp.fortis.developmentbooks.service.DevelopmentBooksService;
import com.bnpp.fortis.developmentbooks.service.impl.PriceSummationServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = DevelopmentBooksController.class)
class DevelopmentBooksControllerTest {


    private static final String FIRST_BOOK_NAME = "Clean Code";
    private static final String SECOND_BOOK_NAME = "The Clean Coder";

    private static final int ONE = 1;
    private static final String GET_ALL_BOOKS_URI = "/api/developmentbooks/getallbooks";

    private static final String CALCULATE_PRICE_URI = "/api/developmentbooks/calculatediscountprice";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private DevelopmentBooksController developmentBooksController;

    @MockBean
    private PriceSummationServiceImpl priceSummationServiceImpl;
    @MockBean
    private DevelopmentBooksService developmentBooksService;

    @Test
    @DisplayName("development book controller instance should not be null")
    void developmentControllershouldNotBeNull() {
        Assertions.assertThat(developmentBooksController).isNotNull();
    }

    @Test
    @DisplayName(" API getBooks should return status OK")
    void getBooksApiShouldReturnStatusOK() throws Exception {
        mockMvc.perform(get(GET_ALL_BOOKS_URI))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName(" API calculate discount price should return status OK")
    void calculateDiscountPrice_Api_shouldReturn_StatusOK() throws Exception {
        List<BookDto> listOfBooks = new ArrayList<BookDto>();

        BookDto firstBook = new BookDto(FIRST_BOOK_NAME, ONE);
        BookDto secondBook = new BookDto(SECOND_BOOK_NAME, ONE);

        listOfBooks.add(firstBook);
        listOfBooks.add(secondBook);

        mockMvc.perform(post(CALCULATE_PRICE_URI).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(listOfBooks))).andExpect(status().isOk());
    }


}