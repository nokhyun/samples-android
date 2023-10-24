package com.nokhyun.fakepaging

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private lateinit var test: String

//    @Test
//    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)
//    }

    @Test
    fun `LATE_INIT_체크`(){
        test = ""
        assertEquals(true, ::test.isInitialized)
    }
}