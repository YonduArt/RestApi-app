package com.example.repository

import com.example.Entity.City
import com.example.Entity.Country
import org.springframework.data.repository.CrudRepository

interface CityRepository: CrudRepository<City, Int> {

    fun deleteAllByCountry(country: Country)
}