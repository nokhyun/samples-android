package com.nokhyun.fakepaging

import com.nokhyun.network_paging.Airline
import com.nokhyun.network_paging.Passenger
import com.nokhyun.network_paging.PassengerResponse
import com.nokhyun.passenger.PassengerEntity

internal object PassengerMapper {
    fun com.nokhyun.network_paging.PassengerResponse.toEntity() = PassengerEntity(
        totalPassengers = totalPassengers,
        totalPages = totalPages,
        data = data.map { it.toEntity() }
    )

    fun com.nokhyun.network_paging.Passenger.toEntity() = com.nokhyun.passenger.Passenger(
        _id = _id,
        name = name,
        trips = trips,
        airline = airline.map { it.toEntity() },
        __v = __v
    )

    private fun com.nokhyun.network_paging.Airline.toEntity() = com.nokhyun.passenger.Airline(
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