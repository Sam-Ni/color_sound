package com.example.colorsound.util

import com.example.colorsound.R

const val COLOR_NUMBER = 6

val COLORS
= List(10) { index -> if (index % 2 == 0) R.drawable.circle_black  else R.drawable.circle_orange}

const val BASE_URL = "http://192.168.3.10:8080/"