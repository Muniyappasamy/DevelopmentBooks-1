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
    private static final String FOURTH_BOOK_NAME = "Test-Driven Development By Example";
    private static final String FIFTH_BOOK_NAME = "Working Effectively With Legacy Code";

    private static final double TWO_DIFF_BOOK_EXPECTED_PRICE_WITH_2_PER_DISCOUNT = 95.0;
    private static final double THREE_DIFF_BOOK_EXPECTED_PRICE_WITH_10_PER_DISCOUNT = 135.0;
    private static final double FOUR_DISTINCT_BOOKS_PRICE_WITH_TWENTY_PERCENTAGE_DISCOUNT = 160.00;
    private static final double FOUR_DISTINCT_BOOKS_PRICE_WITH_TWENTYFIVE_PERCENTAGE_DISCOUNT = 187.50;
    private static final double NINE_BOOKS_WITH_FIVE_DISTINCT_BOOKS_DISCOUNT = 372.5;

    private static final double TWO_DISTINCT_AND_ONE_SEPARATE_BOOK_WITH_DISCOUNT = 145.00;

    private static final double BOOK_PRICE = 50.00;

    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;


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

        Double actualBookPrice = priceSummationServiceImpl.calculateBookPrice(listOfBooks).getCostEffectivePrice();

        assertEquals(BOOK_PRICE, actualBookPrice);
    }


    @ParameterizedTest
    @CsvSource({"1,50", "2,100", "3,150", "4,200", "10,500"})
    @DisplayName("should return price based on quantity")
    void shouldReturnPriceBasedOnQuantity(int quantity, double expectedPrice) {
        BookDto bookDto = new BookDto(FIRST_BOOK_NAME, quantity);

        listOfBooks.add(bookDto);

        Double actualBookPrice = priceSummationServiceImpl.calculateBookPrice(listOfBooks).getCostEffectivePrice();


        assertEquals(expectedPrice, actualBookPrice);
    }


    @Test
    @DisplayName("Two different listOfBooks should get 5% discount")
    void twoDifferentBooksShouldReturnPriceNinetyFive() {

        BookDto firstBook = new BookDto(FIRST_BOOK_NAME, ONE);
        BookDto secondBook = new BookDto(SECOND_BOOK_NAME, ONE);

        listOfBooks.add(firstBook);
        listOfBooks.add(secondBook);

        Double actualBookPrice = priceSummationServiceImpl.calculateBookPrice(listOfBooks).getCostEffectivePrice();

        assertEquals(TWO_DIFF_BOOK_EXPECTED_PRICE_WITH_2_PER_DISCOUNT, actualBookPrice);
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

        Double actualBookPrice = priceSummationServiceImpl.calculateBookPrice(listOfBooks).getCostEffectivePrice();

        assertEquals(THREE_DIFF_BOOK_EXPECTED_PRICE_WITH_10_PER_DISCOUNT, actualBookPrice);
    }

    @Test
    @DisplayName("Four different listOfBooks should get 20% discount")
    void fourDifferentBooksShouldReturnOneHundredAndSixty() {


        BookDto firstBook = new BookDto(FIRST_BOOK_NAME, ONE);
        BookDto secondBook = new BookDto(SECOND_BOOK_NAME, ONE);
        BookDto thirdBook = new BookDto(THIRD_BOOK_NAME, ONE);
        BookDto fourthBook = new BookDto(FOURTH_BOOK_NAME, ONE);

        listOfBooks.add(firstBook);
        listOfBooks.add(secondBook);
        listOfBooks.add(thirdBook);
        listOfBooks.add(fourthBook);

        Double actualBookPrice = priceSummationServiceImpl.calculateBookPrice(listOfBooks).getCostEffectivePrice();


        assertEquals(FOUR_DISTINCT_BOOKS_PRICE_WITH_TWENTY_PERCENTAGE_DISCOUNT, actualBookPrice);
    }

    @Test
    @DisplayName("Five different listOfBooks should get 25% discount")
    void fiveDifferentBooksShouldReturnPriceOneHundredAndEightySeven() {


        BookDto firstBook = new BookDto(FIRST_BOOK_NAME, ONE);
        BookDto secondBook = new BookDto(SECOND_BOOK_NAME, ONE);
        BookDto thirdBook = new BookDto(THIRD_BOOK_NAME, ONE);
        BookDto fourthBook = new BookDto(FOURTH_BOOK_NAME, ONE);
        BookDto fifthBook = new BookDto(FIFTH_BOOK_NAME, 1);

        listOfBooks.add(firstBook);
        listOfBooks.add(secondBook);
        listOfBooks.add(thirdBook);
        listOfBooks.add(fourthBook);
        listOfBooks.add(fifthBook);

        Double actualBookPrice = priceSummationServiceImpl.calculateBookPrice(listOfBooks).getCostEffectivePrice();

        assertEquals(FOUR_DISTINCT_BOOKS_PRICE_WITH_TWENTYFIVE_PERCENTAGE_DISCOUNT, actualBookPrice);
    }


    @Test
    @DisplayName("two distinct listOfBooks should only get 5% discount remaining Books with actual price")
    void fivePercentDiscountOnlyForTwoDistinctBooksShouldApply() {


        BookDto firstBook = new BookDto(FIRST_BOOK_NAME, TWO);
        BookDto secondBook = new BookDto(SECOND_BOOK_NAME, ONE);

        listOfBooks.add(firstBook);
        listOfBooks.add(secondBook);


        Double actualBookPrice = priceSummationServiceImpl.calculateBookPrice(listOfBooks).getCostEffectivePrice();


        assertEquals(TWO_DISTINCT_AND_ONE_SEPARATE_BOOK_WITH_DISCOUNT, actualBookPrice);
    }


    @Test
    @DisplayName("apply discount to all distinct listOfBooks only ")
    void ApplyDiscountToAllDistinctBooks() {

        BookDto firstBook = new BookDto(FIRST_BOOK_NAME, TWO);
        BookDto secondBook = new BookDto(SECOND_BOOK_NAME, ONE);
        BookDto thirdBook = new BookDto(THIRD_BOOK_NAME, THREE);
        BookDto fourthBook = new BookDto(FOURTH_BOOK_NAME, TWO);
        BookDto fifthBook = new BookDto(FIFTH_BOOK_NAME, ONE);

        listOfBooks.add(firstBook);
        listOfBooks.add(secondBook);
        listOfBooks.add(thirdBook);
        listOfBooks.add(fourthBook);
        listOfBooks.add(fifthBook);


        Double actualBookPrice = priceSummationServiceImpl.calculateBookPrice(listOfBooks).getCostEffectivePrice();

        assertEquals(NINE_BOOKS_WITH_FIVE_DISTINCT_BOOKS_DISCOUNT, actualBookPrice);
    }

}