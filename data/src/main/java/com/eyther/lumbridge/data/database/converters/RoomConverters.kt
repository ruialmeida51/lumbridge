package com.eyther.lumbridge.data.database.converters

import androidx.room.TypeConverter
import com.eyther.lumbridge.data.model.shopping.entity.ShoppingListEntryEntity
import com.eyther.lumbridge.shared.time.extensions.toIsoLocalDateString
import com.eyther.lumbridge.shared.time.extensions.toLocalDate
import com.eyther.lumbridge.shared.time.model.Periodicity
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.JsonStreamParser
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.LocalDate

class RoomConverters {
    /**
     * Unfortunately, I'm not sure how to inject this Gson instance into this class as Room needs
     * to be able to create an instance of this class without any parameters. I'm going to leave
     * this as is for now. /shrug
     */
    private val gson: Gson = Gson()

    @TypeConverter
    fun fromStringToShoppingListEntry(value: String?): ArrayList<ShoppingListEntryEntity> {
        val listType: Type = object : TypeToken<ArrayList<ShoppingListEntryEntity?>?>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromShoppingListEntryToString(list: ArrayList<ShoppingListEntryEntity?>?): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToLocalDate(value: String): LocalDate {
        return value.toLocalDate()
    }

    @TypeConverter
    fun fromLocalDateToString(date: LocalDate): String {
        return date.toIsoLocalDateString()
    }
}
