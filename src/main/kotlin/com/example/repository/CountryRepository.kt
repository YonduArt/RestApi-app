package com.example.repository

import com.example.Entity.Country
import com.example.model.NameOnly
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository


interface CountryRepository: CrudRepository<Country, Int> {

    fun findByOrderByName(pageable: Pageable): List<Country>

    fun findByNameStartsWithIgnoreCaseOrderByName(prefix: String): List<Country>

    fun findAllByOrderByName(): List<NameOnly>
}
