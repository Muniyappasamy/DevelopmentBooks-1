package com.bnpp.fortis.developmentbooks.service.impl;

import com.bnpp.fortis.developmentbooks.model.BookDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PriceSummationServiceImplTest {

    private static final String FIRST_BOOK_NAME = "Clean Code";
    private static final String SECOND_BOOK_NAME = "The Clean Coder";
    private static final String THIRD_BOOK_NAME = "Clean Architecture";
    private static final double TWO_DIFF_BOOK_EXPECTED_PRICE_WITH_2_PER_DISCOUNT = 95.0;
    private static final double  THREE_DIFF_BOOK_EXPECTED_PRICE_WITH_10_PER_DISCOUNT = 135.0;
    private static final double BOOK_PRICE = 50.00;

    private static final int ONE = 1;
    private static final int TWO = 2;

    List<BookDto> listOfBooks;

    @Autowired
    private PriceSummationServiceImpl priceSummationServiceImpl;

    @BeforeEach
    void setup() {
        listOfBooks = new ArrayList<>();
    }


    @Test
    @DisplayName("single book price should return 50")
    void singleBookCalculatedPriceIsFifty() {
        BookDto bookDto = new BookDto(FIRST_BOOK_NAME, ONE);

        listOfBooks.add(bookDto);

        Double actualBookPrice = priceSummationServiceImpl.calculateBookPrice(listOfBooks);

        assertEquals(BOOK_PRICE, actualBookPrice);
    }


    @ParameterizedTest
    @CsvSource({"1,50", "2,100", "3,150", "4,200", "10,500"})
    @DisplayName("should return price based on quantity")
    void shouldReturnPriceBasedOnQuantity(int quantity, double expectedPrice) {
        BookDto bookDto = new BookDto(FIRST_BOOK_NAME, quantity);

        listOfBooks.add(bookDto);

        Double actualBookPrice = priceSummationServiceImpl.calculateBookPrice(listOfBooks);

        assertEquals(expectedPrice, actualBookPrice);
    }


    @Test
    @DisplayName("Two different listOfBooks should get 5% discount")
    void twoDifferentBooksShouldReturnPriceNinetyFive() {

        BookDto firstBook = new BookDto(FIRST_BOOK_NAME, ONE);
        BookDto secondBook = new BookDto(SECOND_BOOK_NAME, ONE);

        listOfBooks.add(firstBook);
        listOfBooks.add(secondBook);

        Double actualPrice = priceSummationServiceImpl.calculateBookPrice(listOfBooks);

        assertEquals(TWO_DIFF_BOOK_EXPECTED_PRICE_WITH_2_PER_DISCOUNT, actualPrice);
    }


    @Test
    @DisplayName("Three different listOfBooks should get 10% discount")
    void threeDifferentBooksShouldReturnOneHundredAndThirtyFive() {


        BookDto firstBook = new BookDto(FIRST_BOOK_NAME, ONE);
        BookDto secondBook = new BookDto(SECOND_BOOK_NAME, ONE);
        BookDto thirdBook = new BookDto(THIRD_BOOK_NAME, ONE);

        listOfBooks.add(firstBook);
        listOfBooks.add(secondBook);
        listOfBooks.add(thirdBook);

        Double actualPrice = priceSummationServiceImpl.calculateBookPrice(listOfBooks);

        assertEquals(THREE_DIFF_BOOK_EXPECTED_PRICE_WITH_10_PER_DISCOUNT, actualPrice);
    }
}