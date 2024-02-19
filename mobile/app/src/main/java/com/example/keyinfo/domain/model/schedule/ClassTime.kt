package com.example.keyinfo.domain.model.schedule

import java.time.LocalTime

data class ClassTime(
    var name: String, var startTime: LocalTime, var endTime: LocalTime
)