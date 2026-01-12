package com.example.exception

import org.springframework.http.HttpStatus

class CountryNotFoundException(countryId: Int) : BaseException(
    HttpStatus.NOT_FOUND,
    ApiError(
        errorCode = "country.not.found",
        description = "Country not found with id=$countryId"
    )
)