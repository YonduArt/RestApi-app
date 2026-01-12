package com.example.service.impl

import com.example.dto.CityDto
import com.example.dto.CountryDto
import com.example.exception.CountryNotFoundException
import com.example.Entity.City
import com.example.Entity.Country
import com.example.repository.CityRepository
import com.example.repository.CountryRepository
import com.example.service.CountryService
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CountryServiceImpl(
    private val countryRepository: CountryRepository,
    private val cityRepository: CityRepository,
) : CountryService {

    override fun getAll(pageIndex: Int): List<CountryDto> {
        return countryRepository.findByOrderByName(PageRequest.of(pageIndex, 2))
            .map { it.toDto() }
    }

    override fun getById(id: Int): CountryDto {
        return countryRepository.findByIdOrNull(id)
            ?.toDto()
            ?: throw CountryNotFoundException(id)
    }

    override fun search(prefix: String): List<CountryDto> =
        countryRepository.findByNameStartsWithIgnoreCaseOrderByName(prefix)
            .map { it.toDto() }

    override fun getCountryNames(): List<String> =
        countryRepository.findAllByOrderByName().map { it.name }

    @Transactional
    override fun create(dto: CountryDto): Int {
        val country = countryRepository.save(dto.toEntity())
        val cities = dto.cities.map { it.toEntity(country) }
        cityRepository.saveAll(cities)
        return country.id
    }

    @Transactional
    override fun update(id: Int, dto: CountryDto) {
        var existingCountry = countryRepository.findByIdOrNull(id)
            ?: throw CountryNotFoundException(id)

        existingCountry.name = dto.name
        existingCountry.population = dto.population

        existingCountry = countryRepository.save(existingCountry)

        val cities = dto.cities.map { it.toEntity(existingCountry) }
        cityRepository.deleteAllByCountry(existingCountry)
        cityRepository.saveAll(cities)
    }

    @Transactional
    override fun delete(id: Int) {
        val existingCountry = countryRepository.findByIdOrNull(id)
            ?: throw CountryNotFoundException(id)

        cityRepository.deleteAllByCountry(existingCountry)
        countryRepository.deleteById(existingCountry.id)
    }

    private fun Country.toDto(): CountryDto =
        CountryDto(
            id = this.id,
            name = this.name,
            population = this.population,
            cities = this.cities.map { it.toDto() },
        )

    private fun City.toDto(): CityDto =
        CityDto(
            name = this.name,
        )

    private fun CountryDto.toEntity(): Country =
        Country(
            id = 0,
            name = this.name,
            population = this.population,
        )

    private fun CityDto.toEntity(country: Country): City =
        City(
            id = 0,
            name = this.name,
            country = country,
        )
}