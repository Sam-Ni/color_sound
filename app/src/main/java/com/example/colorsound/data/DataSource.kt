package com.example.colorsound.data

import com.example.colorsound.R
import com.example.colorsound.model.Sound

object DataSource {
    val soundList: List<Sound> = List(10) { it ->
        Sound(R.drawable.circle, it.toString(), "12", it.toString(), "1")
    }
}

