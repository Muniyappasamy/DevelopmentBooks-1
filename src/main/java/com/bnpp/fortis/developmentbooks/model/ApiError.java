package com.bnpp.fortis.developmentbooks.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ApiError {

    String functionalErrorCode;
    String functionalErrorMessage;
}
