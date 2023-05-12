package com.bnpp.fortis.developmentbooks.service;

import com.bnpp.fortis.developmentbooks.model.BookDto;

public interface PriceSummationService {
    public Double calculateBookPrice(BookDto bookDto);
}
