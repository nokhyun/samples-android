package com.nokhyun.bddexam

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe


class FunSpecTest : FunSpec({
    val sum = { x: Int, y: Int -> x + y }

    test("1과 2를 더하면 3이 된다") {
        val stub = sum(1, 2)
        stub shouldBe 3
    }
})