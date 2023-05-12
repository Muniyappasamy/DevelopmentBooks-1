package com.bnpp.fortis.developmentbooks.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = DevelopmentBooksController.class)
class DevelopmentBooksControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private DevelopmentBooksController developmentBooksController;


    @Test
    @DisplayName("development book controller instance should not be null")
    void developmentControllershouldNotBeNull() {
        Assertions.assertThat(developmentBooksController).isNotNull();
    }

    @Test
    @DisplayName(" API getBooks should return status OK")
    void getBooksApiShouldReturnStatusOK() throws Exception {
        mockMvc.perform(get("/api/developmentbooks/getallbooks"))
                .andExpect(status().isOk());

    }


}