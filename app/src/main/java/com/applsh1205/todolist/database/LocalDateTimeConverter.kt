package com.applsh1205.todolist.database

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class LocalDateTimeConverter {

    @TypeConverter
    fun longToLocalDateTime(millis: Long?): LocalDateTime? {
        if (millis == null) return null
        return Instant.ofEpochMilli(millis)
            .atZone(ZoneId.of("UTC"))
            .toLocalDateTime()
    }

    @TypeConverter
    fun toDateLong(localDateTime: LocalDateTime?): Long? {
        if (localDateTime == null) return null
        return localDateTime.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
    }

}
