package com.nokhyun.fakepaging

import com.nokhyun.passenger.PassengerEntity

internal object PassengerMapper {
    fun PassengerResponse.toEntity() = PassengerEntity(
        totalPassengers = totalPassengers,
        totalPages = totalPages,
        data = data.map { it.toEntity() }
    )

    fun Passenger.toEntity() = com.nokhyun.passenger.Passenger(
        _id = _id,
        name = name,
        trips = trips,
        airline = airline.map { it.toEntity() },
        __v = __v
    )

    private fun Airline.toEntity() = com.nokhyun.passenger.Airline(
        id = id,
        name = name,
        country = country,
        logo = logo,
        slogan = slogan,
        head_quaters = head_quaters,
        website = website,
        established = established
    )
}