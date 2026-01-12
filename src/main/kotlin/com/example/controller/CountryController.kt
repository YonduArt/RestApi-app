package com.example.controller

import com.example.dto.CountryDto
import com.example.service.CountryService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/countries")
class CountryController(
    private val countryService: CountryService,
) {

    @GetMapping
    fun getAll(@RequestParam("page") pageIndex: Int): List<CountryDto> =
        countryService.getAll(pageIndex)

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Int): CountryDto =
        countryService.getById(id)

    @GetMapping("/search")
    fun searchCountries(@RequestParam("prefix") prefix: String): List<CountryDto> =
        countryService.search(prefix)

    @GetMapping("/names")
    fun getCountryNames(): List<String> = countryService.getCountryNames()

    @PostMapping
    fun create(@RequestBody dto: CountryDto): Int {
        return countryService.create(dto)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody dto: CountryDto) {
        return countryService.update(id, dto)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int) {
        return countryService.delete(id)
    }
}