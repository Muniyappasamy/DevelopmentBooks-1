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


    @Override
    public Double calculateBookPrice(List<BookDto> listOfBooks) {
        Map<String, Double> bookTitlePriceMap = Arrays.stream(BookStoreEnum.values())
                .collect(Collectors.toMap(BookStoreEnum::getBookTitle, BookStoreEnum::getPrice));
        long distinctBooks = listOfBooks.stream().map(BookDto::getName).distinct().count();
        int discountPercentage = 0;
        if(distinctBooks == 2){
            discountPercentage = 5;
        } else if (distinctBooks == 3) {
            discountPercentage = 10;
        }

        double actualPrice = listOfBooks.stream()
                .mapToDouble(book -> bookTitlePriceMap.get(book.getName()) * book.getQuantity()).sum();
        double discountedPrice = (actualPrice * discountPercentage) / 100;

        return (actualPrice - discountedPrice);
    }
}
