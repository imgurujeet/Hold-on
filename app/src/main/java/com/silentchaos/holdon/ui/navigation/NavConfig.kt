package com.silentchaos.holdon.ui.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

val navConfig = SavedStateConfiguration {

    serializersModule = SerializersModule {

        polymorphic(NavKey ::class) {

            subclass(Home::class,Home.serializer())
            subclass(Setting::class,Setting.serializer())
            subclass(Info::class,Info.serializer())

        }
    }
}