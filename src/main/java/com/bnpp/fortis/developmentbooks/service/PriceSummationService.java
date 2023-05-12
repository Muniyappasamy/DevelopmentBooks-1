package com.bnpp.fortis.developmentbooks.service;

import com.bnpp.fortis.developmentbooks.model.BookDto;
import com.bnpp.fortis.developmentbooks.model.CartSummaryReportDto;

import java.util.List;

public interface PriceSummationService {
    public CartSummaryReportDto getCartSummaryReport(List<BookDto> listOfBooks);
}
