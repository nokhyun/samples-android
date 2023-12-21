package com.nokhyun.first

import com.nokhyun.startup_api.Initializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface FirstModule {

    @IntoSet
    @Binds
    fun bindsInitializer(impl: FirstInitializer): Initializer
}