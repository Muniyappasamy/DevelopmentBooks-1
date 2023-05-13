package com.bnpp.fortis.developmentbooks.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BooksData {
    @ApiModelProperty(notes = "List of Books with Quantities", name = "bookList", required = true, value = "list of books")
    List<BookDto> bookList;
}
