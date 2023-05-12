package com.bnpp.fortis.developmentbooks.service.impl;

import com.bnpp.fortis.developmentbooks.model.BookDto;
import com.bnpp.fortis.developmentbooks.service.PriceSummationService;
import com.bnpp.fortis.developmentbooks.storerepository.BookStoreEnum;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PriceSummationServiceImpl implements PriceSummationService {

    private static final int ZERO = 0;
    private static final int TWO = 2;
    private static final int THREE = 3;

    private static final int FIVE = 5;
    private static final int TEN = 10;

    private static final int HUNDRED = 100;


    @Override
    public Double calculateBookPrice(List<BookDto> listOfBooks) {
        Map<String, Double> bookTitlePriceMap = Arrays.stream(BookStoreEnum.values())
                .collect(Collectors.toMap(BookStoreEnum::getBookTitle, BookStoreEnum::getPrice));
        long distinctBooks = listOfBooks.stream().map(BookDto::getName).distinct().count();
        int discountPercentage = ZERO;
        if (distinctBooks == TWO) {
            discountPercentage = FIVE;
        } else if (distinctBooks == THREE) {
            discountPercentage = TEN;
        }

        double actualPrice = listOfBooks.stream()
                .mapToDouble(book -> bookTitlePriceMap.get(book.getName()) * book.getQuantity()).sum();
        double discountedPrice = (actualPrice * discountPercentage) / HUNDRED;

        return (actualPrice - discountedPrice);
    }

}
