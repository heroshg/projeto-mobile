package com.ti4all.agendaapp.data

import androidx.room.TypeConverter
import java.util.Date

 class DateConverter{
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}