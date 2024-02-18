package com.example.keyinfo.domain.model.schedule

import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.OffsetDateTime

data class Audience(
    @SerializedName("build_id") val building: Int,
    @SerializedName("room_id") val audience: Int,
    @SerializedName("start_time") @JsonAdapter(OffsetDateTimeAdapter::class) val startTime: OffsetDateTime,
    @SerializedName("end_time") @JsonAdapter(OffsetDateTimeAdapter::class) val endTime: OffsetDateTime,
    val status: AudienceStatus
)

data class ReserveAudienceRequest(
    @SerializedName("build_id") val building: Int,
    @SerializedName("room_id") val audience: Int,
    @SerializedName("start_time") @JsonAdapter(OffsetDateTimeAdapter::class) val startTime: OffsetDateTime,
    @SerializedName("end_time") @JsonAdapter(OffsetDateTimeAdapter::class) val endTime: OffsetDateTime,
    @SerializedName("under_which_role_perform") val role: String,
    @SerializedName("is_duplicate") val isDuplicate: Boolean = false,
    @SerializedName("until_when_duplicate") val untilWhenDuplicate: OffsetDateTime? = null
)

data class AudienceRequest(
    val startTime: OffsetDateTime,
    val endTime: OffsetDateTime,
    val building: Int,
    val audience: Int? = null,
)

class OffsetDateTimeAdapter : TypeAdapter<OffsetDateTime>() {
    override fun write(out: JsonWriter, value: OffsetDateTime?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.toString())
        }
    }

    override fun read(`in`: JsonReader): OffsetDateTime? {
        return OffsetDateTime.parse(`in`.nextString())
    }
}