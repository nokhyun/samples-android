package com.nokhyun.network_paging

import kotlinx.serialization.Serializable

@Serializable
data class PassengerResponse(
    val totalPassengers: Int,
    val totalPages: Int,
    val data: List<Passenger>
)

@Serializable
data class Passenger(
    val _id: String,
    val name: String,
    val trips: Int,
    val airline: List<Airline>,
    val __v: Int
)

@Serializable
data class Airline(
    val _id: String,
    val id: Int,
    val name: String,
    val country: String,
    val logo: String,
    val slogan: String,
    val head_quaters: String,
    val website: String,
    val established: String,
    val __v: Int
)
