package com.nokhyun.passenger

data class PassengerEntity(
    val totalPassengers: Int,
    val totalPages: Int,
    val data: List<Passenger>
)

data class Passenger(
    val _id: String,
    val name: String,
    val trips: Int,
    val airline: List<Airline>,
    val __v: Int
)

data class Airline(
    val id: Int,
    val name: String,
    val country: String,
    val logo: String,
    val slogan: String,
    val head_quaters: String,
    val website: String,
    val established: String
)