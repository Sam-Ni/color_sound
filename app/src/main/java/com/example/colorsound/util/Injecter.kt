package com.example.colorsound.util

class Injecter private constructor(private val container: MutableMap<String, Any>) {
    fun add(label: String, obj: Any) {
        container[label] = obj
    }

    fun <T> get(label: String): T {
        val obj = container[label]
        return obj as T
    }

    companion object {
        fun instance() = Helper.instance
    }

    private object Helper {
        val instance = Injecter(HashMap())
    }

}
