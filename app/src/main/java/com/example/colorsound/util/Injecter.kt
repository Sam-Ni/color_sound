package com.example.colorsound.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.reflect.KType
import kotlin.reflect.typeOf

object Injecter {
    val container: MutableMap<KType, Any> = HashMap()
    val serviceContainer: MutableMap<KType, Any> = HashMap()

    inline fun <reified T : Any> add(obj: T) {
        container[typeOf<T>()] = obj
    }

    inline fun <reified T : Any> get(): T {
        return container[typeOf<T>()] as T
    }

    inline fun <reified T : Any> getMutable(): MutableStateFlow<T> {
        return container[typeOf<MutableStateFlow<T>>()] as MutableStateFlow<T>
    }


    inline fun <reified T : Any> addService(obj: T) {
        serviceContainer[typeOf<T>()] = obj
    }

    inline fun <reified T : Any> getService(): T {
        return serviceContainer[typeOf<T>()] as T
    }

}
