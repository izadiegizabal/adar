package xyz.izadi.adar.data.local.room

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromInstant(instant: Instant?): String? {
        return instant?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun stringToInstant(value: String?): Instant? {
        return value?.let { Json.decodeFromString<Instant>(it) }
    }
}