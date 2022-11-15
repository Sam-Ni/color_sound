package com.example.colorsound.data

import com.example.colorsound.R

object DataSource {
    val soundList: List<SoundInfo> = List(10) { it ->
        SoundInfo(R.drawable.circle, it.toString(), "12", it.toString())
    }
}

