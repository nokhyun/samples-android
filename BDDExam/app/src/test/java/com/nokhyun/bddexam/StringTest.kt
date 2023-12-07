package com.nokhyun.bddexam

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class StringTest : StringSpec() {
    init {
        "strings.length should return size of string" { // code desc
            "hello".length shouldBe 4   // check
        }
    }
}