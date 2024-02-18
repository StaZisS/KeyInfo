package com.example.keyinfo.common

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import java.util.Date
import java.util.Locale

object Formatter {
    fun formatDate(date: Date?): String {
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return if (date != null) {
            val utilDate = if (date > Calendar.getInstance().time) {
                Calendar.getInstance().time
            } else {
                date
            }
            formatter.format(utilDate)
        } else {
            Constants.EMPTY_STRING
        }
    }

    fun formatDateToISO8601(date: Date?): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return if (date != null) {
            val utilDate = if (date > Calendar.getInstance().time) {
                Calendar.getInstance().time
            } else {
                date
            }
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            formatter.format(utilDate)
        } else {
            Constants.EMPTY_STRING
        }
    }


    fun formatDateToNormal(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date!!)
    }

    fun getNormalRoleString(input: ArrayList<String>): String {
        var output = ""
        input.forEach{ input ->
            when(input){
                "ADMIN" -> output += "Администратор\n"
                "STUDENT" -> output += "Студент\n"
                "TEACHER" -> output += "Преподаватель\n"
                "UNSPECIFIED" -> output += "Не подтвержден\n"
                "DEANERY" -> output += "Деканат\n"
            }
        }
        return output;
    }
}