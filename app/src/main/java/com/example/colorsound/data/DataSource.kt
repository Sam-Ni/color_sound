package com.example.colorsound.data

import com.example.colorsound.model.Sound

object DataSource {
    val soundList: List<Sound> = List(10) { it ->
        Sound(1, 1, it.toString() + "a3A5你好8l?$", "2021-12-10", it.toString())
    }
}

