package com.example.colorsound.util

object Injecter {
    private val container: MutableMap<String, Any> = HashMap()

    fun add(label: String, obj: Any) {
        container[label] = obj
    }

    fun <T> get(label: String): T {
        val obj = container[label]
        return obj as T
    }
}
