package com.example.Entity

import jakarta.persistence.*

@Entity
@Table(name="city")
class City (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    var name: String = "",
    @ManyToOne
    @JoinColumn(name = "country_id")
    var country: Country,
)