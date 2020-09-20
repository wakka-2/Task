package com.task.app.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


/*
Created by Aiman Qaid on 20,سبتمبر,2020
Contact me at wakka-2@hotmail.com
*/
object Tools {
    fun isSaudiNumber(userMobile: String): Boolean {
        return when (userMobile.length) {
            10 -> {
                userMobile.startsWith("05")
            }
//            9 -> {
//                userMobile.startsWith("5")
//            }
//            12 -> {
//                userMobile.startsWith("9665")
//            }
//            13 -> {
//                userMobile.startsWith("+9665")
//            }
            else -> false
        }
    }

    fun validateEmail(email: String): Boolean {
        return Pattern
            .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
            .matcher(email)
            .matches()
    }
}