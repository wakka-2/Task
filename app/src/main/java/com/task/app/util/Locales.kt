package com.task.app.util

import java.util.*

object Locales {
    val arabic: Locale by lazy { Locale("ar", "AR") }
    val english: Locale by lazy { Locale("en", "EN") }

    val RTL: Set<String> by lazy {
        hashSetOf(
            "ar",
            "dv",
            "fa",
            "ha",
            "he",
            "iw",
            "ji",
            "ps",
            "sd",
            "ug",
            "ur",
            "yi"
        )
    }
}