package com.bnpp.fortis.developmentbooks.service;

import com.bnpp.fortis.developmentbooks.model.BookDto;

import java.util.List;

public interface PriceSummationService {
    public Double calculateBookPrice(List<BookDto> listOfbooks);
}
