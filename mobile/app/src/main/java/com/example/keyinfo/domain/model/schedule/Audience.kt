package com.example.keyinfo.domain.model.schedule

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.time.OffsetDateTime

data class Audience(
    @SerializedName("build_id") val building: Int,
    @SerializedName("room_id") val audience: Int,
    @SerializedName("start_time") @JsonAdapter(OffsetDateTimeAdapter::class) val startTime: OffsetDateTime,
    @SerializedName("end_time") @JsonAdapter(OffsetDateTimeAdapter::class) val endTime: OffsetDateTime,
    val status: AudienceStatus
)

