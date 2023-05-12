package com.bnpp.fortis.developmentbooks.service.impl;

import com.bnpp.fortis.developmentbooks.exception.InvalidBookException;
import com.bnpp.fortis.developmentbooks.model.BookDto;
import com.bnpp.fortis.developmentbooks.model.BookGroupClassification;
import com.bnpp.fortis.developmentbooks.model.CartSummaryReportDto;
import com.bnpp.fortis.developmentbooks.service.PriceSummationService;
import com.bnpp.fortis.developmentbooks.storerepository.BookStoreEnum;
import com.bnpp.fortis.developmentbooks.storerepository.DiscountDetailsEnum;
import org.apache.commons.collections4.CollectionUtils;
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

        validateAllBooks(listOfBooks);

        Map<String, Integer> listOfBooksWithQuantityMap = listOfBooks.stream()
                .collect(Collectors.toMap(BookDto::getName, BookDto::getQuantity));
        List<Integer> listOfPossibleDiscounts = getPossibleDiscountValues(listOfBooksWithQuantityMap.size());
        CartSummaryReportDto cartSummaryReportDto = new CartSummaryReportDto();
        if (CollectionUtils.isNotEmpty(listOfPossibleDiscounts)) {
            calculateAndUpdatePriceWithDiscount(listOfBooksWithQuantityMap, listOfPossibleDiscounts, cartSummaryReportDto);
        } else {
            calculateAndUpdatePriceWithOutDiscount(listOfBooksWithQuantityMap, cartSummaryReportDto);
        }
        return cartSummaryReportDto;
    }
    public void validateAllBooks(List<BookDto> listOfBooks) {
        Map<String, Double> validBooks = getBookNamePriceMap(); //
        List<String> invalidBooks = listOfBooks.stream().filter(book -> !validBooks.containsKey(book.getName())).map(BookDto::getName).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(invalidBooks)) {
            throw new InvalidBookException(invalidBooks);
        }
    }
    public void calculateAndUpdatePriceWithOutDiscount(Map<String, Integer> listOfbooksWithQuantityMap, CartSummaryReportDto priceSummaryDto) {
        BookGroupClassification booksWithoutDiscount = getListOfBookGroupWithoutDiscount(listOfbooksWithQuantityMap);
        List<BookGroupClassification> listOfBookGroupClassification = new ArrayList<>();
        listOfBookGroupClassification.add(booksWithoutDiscount);
        updateBestDiscount(priceSummaryDto, listOfBookGroupClassification);
    }

    public void calculateAndUpdatePriceWithDiscount(Map<String, Integer> listOfbooksWithQuantityMap, List<Integer> listOfPossibleDiscounts, CartSummaryReportDto cartSummaryReportDto) {
        listOfPossibleDiscounts.stream().forEach(numberOfBooksToGroup -> {
            Map<String, Integer> bookQuantityMapCopy = duplicateMap(listOfbooksWithQuantityMap);
            List<BookGroupClassification> listOfBookGroupClassification = getListOfBookGroupWithDiscount(bookQuantityMapCopy, new ArrayList<BookGroupClassification>(), numberOfBooksToGroup);
            if (CollectionUtils.isNotEmpty(bookQuantityMapCopy.keySet())) {
                BookGroupClassification booksWithoutDiscount = getListOfBookGroupWithoutDiscount(bookQuantityMapCopy);
                listOfBookGroupClassification.add(booksWithoutDiscount);
            }
            updateBestDiscount(cartSummaryReportDto, listOfBookGroupClassification);
        });
    }

    private void updateBestDiscount(CartSummaryReportDto priceSummaryDto, List<BookGroupClassification> listOfBookGroupClassification) {
        double discount = listOfBookGroupClassification.stream().mapToDouble(BookGroupClassification::getDiscountAmount).sum();
        if (discount >= priceSummaryDto.getTotalDiscount()) {
            priceSummaryDto.setListOfBookGroupClassifications(listOfBookGroupClassification);
            double actualPrice = listOfBookGroupClassification.stream().mapToDouble(BookGroupClassification::getActualPrice).sum();
            priceSummaryDto.setActualPrice(actualPrice);
            priceSummaryDto.setTotalDiscount(discount);
            priceSummaryDto.setCostEffectivePrice(actualPrice - discount);
        }
    }

    public List<Integer> getPossibleDiscountValues(int numberOfBooks) {

        return Arrays.stream(DiscountDetailsEnum.values()).sorted(Comparator.reverseOrder()).filter(discountGroup -> discountGroup.getNumberOfDistinctItems() <= numberOfBooks).map(DiscountDetailsEnum::getNumberOfDistinctItems).collect(Collectors.toList());
    }

    private List<BookGroupClassification> getListOfBookGroupWithDiscount(Map<String, Integer> listOfBooksWithQuantityMap, List<BookGroupClassification> bookGroupClassificationList, int numberOfBooksToGroup) {

        numberOfBooksToGroup = getNumberOfBooksToGroup(listOfBooksWithQuantityMap, numberOfBooksToGroup);
        Optional<DiscountDetailsEnum> discount = getDiscount(numberOfBooksToGroup);
        if (discount.isPresent()) {
            int bookGroupSize = discount.get().getNumberOfDistinctItems();
            List<String> listOfDistinctBooks = listOfBooksWithQuantityMap.keySet().stream().limit(bookGroupSize)
                    .collect(Collectors.toList());
            BookGroupClassification currentBookGroup = getBookGroup(listOfDistinctBooks);
            bookGroupClassificationList.add(currentBookGroup);
            removeDiscountedBooksFromMap(listOfBooksWithQuantityMap, listOfDistinctBooks);
            getListOfBookGroupWithDiscount(listOfBooksWithQuantityMap, bookGroupClassificationList, numberOfBooksToGroup);
        }
        return bookGroupClassificationList;
    }

    private int getNumberOfBooksToGroup(Map<String, Integer> bookQuantityMap, Integer numberOfBooksToGroup) {
        return numberOfBooksToGroup < bookQuantityMap.size() ? numberOfBooksToGroup : bookQuantityMap.size();
    }

    private Map<String, Integer> duplicateMap(Map<String, Integer> bookQuantityMap) {

        return bookQuantityMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (firstKey, secondKey) -> secondKey, LinkedHashMap::new));
    }

    private BookGroupClassification getBookGroup(List<String> listOfBookToGroup) {
        Map<String, Double> bookNamePriceMap = getValidBooks();
        double actualPrice = listOfBookToGroup.stream().mapToDouble(bookId -> bookNamePriceMap.get(bookId) * ONE_QUANTITY)
                .sum();
        int discountPercentage = getDiscountPercentage(listOfBookToGroup.size());
        double discount = (actualPrice * discountPercentage) / HUNDRED;
        return new BookGroupClassification(listOfBookToGroup, discountPercentage, actualPrice, discount, listOfBookToGroup.size());

    }

    private Map<String, Double> getValidBooks() {
        return Arrays.stream(BookStoreEnum.values())
                .collect(Collectors.toMap(BookStoreEnum::getBookTitle, BookStoreEnum::getPrice));
    }

    private Optional<DiscountDetailsEnum> getDiscount(int numberOfBooks) {

        return Arrays.stream(DiscountDetailsEnum.values()).filter(discountGroup -> discountGroup.getNumberOfDistinctItems() == numberOfBooks).findFirst();
    }

    private BookGroupClassification getListOfBookGroupWithoutDiscount(Map<String, Integer> listOfBooksWithQuantityMap) {
        Map<String, Double> bookIdPriceMap = getBookNamePriceMap();
        Set<String> bookTitles = listOfBooksWithQuantityMap.keySet();
        double actualPrice = bookTitles.stream()
                .mapToDouble(bookId -> bookIdPriceMap.get(bookId) * listOfBooksWithQuantityMap.get(bookId)).sum();
        int numberOfBooks = listOfBooksWithQuantityMap.values().stream().mapToInt(Integer::intValue).sum();
        return new BookGroupClassification(new ArrayList<>(listOfBooksWithQuantityMap.keySet()), ZERO_PERCENT, actualPrice, BigDecimal.ZERO.doubleValue(), numberOfBooks);
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
