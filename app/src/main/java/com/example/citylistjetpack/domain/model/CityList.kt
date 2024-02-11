package com.example.citylistjetpack.domain.model

/*
* "city": "Melbourne",
    "lat": "-37.8142",
    "lng": "144.9631",
    "country": "Australia",
    "iso2": "AU",
    "admin_name": "Victoria",
    "capital": "admin",
    "population": "4529500",
    "population_proper": "4529500"
    * */

data class CityList(
    val city : String,
    val capital : String,
    val population : String,
    val lat : String,
    val lng : String
)
