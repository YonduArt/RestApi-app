package com.example.Entity

import jakarta.persistence.*
import java.util.Collections.emptyList

@Entity
@Table(name="country")
class Country(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    var name: String = "",
    var population: Int = 0,
    @OneToMany(mappedBy = "country")
    var cities: List<City> = emptyList(),
)