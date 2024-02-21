package com.example.keyinfo.domain.model.schedule

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.OffsetDateTime

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