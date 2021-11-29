package com.josemiz.interview.models

import android.graphics.Color

enum class ColorEnum(val type: String) {
    RED("red") {
        override fun getColor(): Int = Color.RED
    },
    BLUE("blue") {
        override fun getColor(): Int = Color.BLUE
    },
    GREEN("green") {
        override fun getColor(): Int = Color.GREEN
    };

    abstract fun getColor(): Int

    companion object {
        fun getColor(color: String) = values().find { it.type.equals(color, ignoreCase = true) }
    }

}