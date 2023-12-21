package com.nokhyun.startup_api

/**
 * 해당 인터페이스를 통해 app 모듈에서 모든 초기화가 이루어질 수 있도록하고, 협업 시
 * app 모듈에 여러명이 초기화단의 코드를 건드리는 일이 없으므로, 컨플릭트의 발생을 낮춤.
 * Hilt를 이용하여 app 모듈에서 feature 모듈에 직접 접근하는 것이 아닌,
 * feature 모듈이 app 모듈에 제공하도록 변경하면된다.
 * 참고: https://speakerdeck.com/fornewid/android-modularization-recipe?slide=88
 * @see com.nokhyun.playground1.PlaygroundApplication
 * @see com.nokhyun.first.FirstInitializer
 * */
interface Initializer {
    operator fun invoke()
}