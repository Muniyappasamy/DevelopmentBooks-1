package com.bnpp.fortis.developmentbooks.service.impl;

import com.bnpp.fortis.developmentbooks.model.BookDto;
import com.bnpp.fortis.developmentbooks.model.BookGroupClassification;
import com.bnpp.fortis.developmentbooks.model.CartSummaryReportDto;
import com.bnpp.fortis.developmentbooks.service.PriceSummationService;
import com.bnpp.fortis.developmentbooks.storerepository.BookStoreEnum;
import com.bnpp.fortis.developmentbooks.storerepository.DiscountDetailsEnum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PriceSummationServiceImpl implements PriceSummationService {

    private static final int ZERO = 0;
    private static final int TWO = 2;
    private static final int THREE = 3;

    private static final int ZERO_PERCENT = 0;

    private static final int FIVE = 5;
    private static final int TEN = 10;

    private static final int ONE_QUANTITY = 1;

    private static final int HUNDRED = 100;


    @Override
    public CartSummaryReportDto getCartSummaryReport(List<BookDto> listOfBooks) {

        Map<String, Integer> listOfBooksWithQuantityMap = listOfBooks.stream()
                .collect(Collectors.toMap(BookDto::getName, BookDto::getQuantity));
        List<BookGroupClassification> listOfBookGroup = getListOfBookGroupWithDiscount(listOfBooksWithQuantityMap, new ArrayList<>());
        BookGroupClassification booksWithoutDiscount = getListOfBookGroupWithoutDiscount(listOfBooksWithQuantityMap);
        listOfBookGroup.add(booksWithoutDiscount);
        double actualPrice = listOfBookGroup.stream().mapToDouble(BookGroupClassification::getActualPrice).sum();
        double discount = listOfBookGroup.stream().mapToDouble(BookGroupClassification::getDiscountAmount).sum();
        CartSummaryReportDto cartSummaryReportDto = new CartSummaryReportDto();
        cartSummaryReportDto.setListOfBookGroupClassifications(listOfBookGroup);
        cartSummaryReportDto.setActualPrice(actualPrice);
        cartSummaryReportDto.setTotalDiscount(discount);
        cartSummaryReportDto.setCostEffectivePrice(actualPrice - discount);
        return cartSummaryReportDto;
    }

    private List<BookGroupClassification> getListOfBookGroupWithDiscount(Map<String, Integer> listOfBooksWithQuantityMap,
                                                                     List<BookGroupClassification> bookGroupClassificationList) {
        Optional<DiscountDetailsEnum> discount = getDiscount(listOfBooksWithQuantityMap.size());
        if (discount.isPresent()) {
            int bookGroupSize = discount.get().getNumberOfDistinctItems();
            List<String> listOfDistinctBooks = listOfBooksWithQuantityMap.keySet().stream().limit(bookGroupSize)
                    .collect(Collectors.toList());
            BookGroupClassification currentBookGroup = getBookGroup(listOfDistinctBooks);
            bookGroupClassificationList.add(currentBookGroup);
            removeDiscountedBooksFromMap(listOfBooksWithQuantityMap, listOfDistinctBooks);
            getListOfBookGroupWithDiscount(listOfBooksWithQuantityMap, bookGroupClassificationList);
        }
        return bookGroupClassificationList;
    }

    private BookGroupClassification getBookGroup(List<String> listOfBookToGroup) {
        Map<String, Double> bookNamePriceMap = getValidBooks();
        double actualPrice = listOfBookToGroup.stream().mapToDouble(bookId -> bookNamePriceMap.get(bookId) * ONE_QUANTITY)
                .sum();
        int discountPercentage = getDiscountPercentage(listOfBookToGroup.size());
        double discount = (actualPrice * discountPercentage) / HUNDRED;
        return new BookGroupClassification(listOfBookToGroup,discountPercentage, actualPrice, discount, listOfBookToGroup.size());

    }

    private Map<String, Double> getValidBooks() {
        return Arrays.stream(BookStoreEnum.values())
                .collect(Collectors.toMap(BookStoreEnum::getBookTitle, BookStoreEnum::getPrice));
    }
    private Optional<DiscountDetailsEnum> getDiscount(int numberOfBooks) {

        return Arrays.stream(DiscountDetailsEnum.values()).filter(discountGroup -> discountGroup.getNumberOfDistinctItems() == numberOfBooks).findFirst();
    }

    private BookGroupClassification  getListOfBookGroupWithoutDiscount(Map<String, Integer> listOfBooksWithQuantityMap) {
        Map<String, Double> bookIdPriceMap = getBookNamePriceMap();
        Set<String> bookTitles = listOfBooksWithQuantityMap.keySet();
        double actualPrice = bookTitles.stream()
                .mapToDouble(bookId -> bookIdPriceMap.get(bookId) * listOfBooksWithQuantityMap.get(bookId)).sum();
        int numberOfBooks = listOfBooksWithQuantityMap.values().stream().mapToInt(Integer::intValue).sum();
        return new BookGroupClassification( new ArrayList<>(listOfBooksWithQuantityMap.keySet()),ZERO_PERCENT,  actualPrice, BigDecimal.ZERO.doubleValue(), numberOfBooks);
    }

    private void removeDiscountedBooksFromMap(Map<String, Integer> itemIdQuantityMap, List<String> discountedItems) {
        discountedItems.forEach(itemId -> {
            int quantity = itemIdQuantityMap.get(itemId);
            if (quantity > ONE_QUANTITY) {
                itemIdQuantityMap.put(itemId, quantity - ONE_QUANTITY);
            } else {
                itemIdQuantityMap.remove(itemId);
            }
        });
    }

    private Map<String, Double> getBookNamePriceMap() {
        return Arrays.stream(BookStoreEnum.values())
                .collect(Collectors.toMap(BookStoreEnum::getBookTitle, BookStoreEnum::getPrice));
    }

    private int getDiscountPercentage(int numberOfDistinctBooks) {
        Optional<DiscountDetailsEnum> checkDiscount = getDiscount(numberOfDistinctBooks);

        return (checkDiscount.isPresent()) ? checkDiscount.get().getDiscountPercentage() : ZERO_PERCENT;
    }


}
