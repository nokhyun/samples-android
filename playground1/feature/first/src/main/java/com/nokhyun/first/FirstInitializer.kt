package com.nokhyun.first

import com.nokhyun.startup_api.Initializer
import javax.inject.Inject

class FirstInitializer @Inject constructor(): Initializer {
    override fun invoke() {
        logger { "FirstInitializer invoke()" }
    }
}