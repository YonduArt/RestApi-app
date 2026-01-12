package com.example.service

import com.example.dto.CountryDto

interface CountryService {

    fun getAll(pageIndex: Int): List<CountryDto>

    fun getById(id: Int): CountryDto

    fun search(prefix: String):  List<CountryDto>

    fun getCountryNames(): List<String>

    fun create(dto: CountryDto): Int

    fun update(id: Int, dto: CountryDto)

    fun delete(id: Int)
}