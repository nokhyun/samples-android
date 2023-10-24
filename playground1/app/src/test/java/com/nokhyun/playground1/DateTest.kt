package com.nokhyun.playground1

import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTest {

    @Test
    fun `요일_날짜_초로_변환시킨다`(){
        val date = "2023.10.20"
        val time = "01 : 00"

        val clearEmptyTime = time.replace(" ", "")

//        val testTime = date + clearEmptyTime
        val testTime = "2023.10.2017:36"
        println("testTime: $testTime")

        val result = LocalDateTime.parse(testTime, DateTimeFormatter.ofPattern("yyyy.M.dHH:mm")).withSecond(LocalDateTime.now().second).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))

        println("result: $result")
    }
}