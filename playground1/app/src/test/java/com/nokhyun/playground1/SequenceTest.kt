package com.nokhyun.playground1

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.random.Random

class SequenceTest {

    @Test
    fun sequenceTest() {
        val result = mutableSetOf<Int>().apply {
            while (this.size < 19) {
                this.add(Random.nextInt(1, 20))
            }
        }
            .asSequence()
//            .takeWhile { it < 80 }
            .filter { it < 50 }
            .take(5)
            .toList()

        println("result: $result")
        assertEquals(5, result.size)
    }
}