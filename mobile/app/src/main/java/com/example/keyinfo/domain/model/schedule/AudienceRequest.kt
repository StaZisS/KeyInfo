package com.example.keyinfo.domain.model.schedule

import java.time.OffsetDateTime

data class AudienceRequest(
    val startTime: OffsetDateTime,
    val endTime: OffsetDateTime,
    val building: Int,
    val audience: Int? = null,
)