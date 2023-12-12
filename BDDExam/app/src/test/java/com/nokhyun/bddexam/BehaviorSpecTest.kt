package com.nokhyun.bddexam

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class BehaviorSpecTest : BehaviorSpec({
    val sum = { x: Int, y: Int -> x + y }

    /*
    * Given JUnit @BeforeEach 와 비슷한 개념으로 생각하면 됨.
    * @BeforeEach: 모든 테스트코드의 중복로직을 실행할 수 있음.
    * */
    Given("sum") {
        val result = sum(1, 2)

        When("1과 2를 더하면") {
            Then("3이 리턴") {
                result shouldBe 3
            }
        }

        When("result의 값과 1 + 2는") {
            Then("값이 같다") {
                result shouldBe sum(1, 2)
            }
        }
    }
})