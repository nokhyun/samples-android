package com.nokhyun.playground1

import android.app.Application
import com.nokhyun.startup_api.Initializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PlaygroundApplication : Application() {
    /**
    * @JvmSuppressWildcards
    * 코틀린 컴파일러는 제네릭을 List<String> -> List<? extends String?> 형태로 변환함
    * 위의 형태로 자동변환을 방지하는 방법을 사용할 때 해당 어노테이션을 사용함.
    * 사용하지 않으면 해당 함수 호출 시 타입을 지정해줘야하는 불편함이 생김.
    * https://github.com/kotlin-korea/Study-Log/issues/24
    * */
    @Inject
    lateinit var initializerSet: Set<@JvmSuppressWildcards Initializer>
}