package com.example.keyinfo.common

import com.example.keyinfo.presentation.screen.schedule.ClassTime
import java.time.LocalTime

object Constants {
    internal const val EMPTY_STRING = ""
    internal const val ZERO = 0
    internal const val FALSE = false
    internal const val NOTHING = "-"
    internal val CLASSES = listOf(
        ClassTime(
            "1 пара",
            LocalTime.of(8, 45, 0, 0),
            LocalTime.of(10, 20, 0, 0)
        ),
        ClassTime(
            "2 пара",
            LocalTime.of(10, 35, 0, 0),
            LocalTime.of(12, 10, 0, 0)
        ),
        ClassTime(
            "3 пара",
            LocalTime.of(12, 25, 0, 0),
            LocalTime.of(14, 0, 0, 0)
        ),
        ClassTime(
            "4 пара",
            LocalTime.of(14, 45, 0, 0),
            LocalTime.of(16, 20, 0, 0)
        ),
        ClassTime(
            "5 пара",
            LocalTime.of(16, 35, 0, 0),
            LocalTime.of(18, 10, 0, 0)
        ),
        ClassTime(
            "6 пара",
            LocalTime.of(18, 25, 0, 0),
            LocalTime.of(20, 0, 0, 0)
        ),
        ClassTime(
            "7 пара",
            LocalTime.of(20, 15, 0, 0),
            LocalTime.of(21, 50, 0, 0)
        ),
    )
}