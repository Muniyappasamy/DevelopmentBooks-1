package com.bnpp.fortis.developmentbooks.service.impl;

import com.bnpp.fortis.developmentbooks.model.BookDto;
import com.bnpp.fortis.developmentbooks.service.PriceSummationService;
import com.bnpp.fortis.developmentbooks.storerepository.BookStoreEnum;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PriceSummationServiceImpl implements PriceSummationService {
    @Override
    public Double calculateBookPrice(BookDto bookDto) {
        Map<String, Double> bookTitlePriceMap = Arrays.stream(BookStoreEnum.values())
                .collect(Collectors.toMap(BookStoreEnum::getBookTitle, BookStoreEnum::getPrice));
        return bookTitlePriceMap.get(bookDto.getName()) * bookDto.getQuantity();
    }
}
