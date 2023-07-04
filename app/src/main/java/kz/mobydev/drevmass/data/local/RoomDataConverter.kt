package kz.mobydev.drevmass.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type
import kz.mobydev.drevmass.model.Lesson


class RoomDataConverter : Serializable {

    @TypeConverter
    fun stringFromObject(list: Any?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun getObjectFromString(jsonString: String?): Any? {
        val listType: Type = object : TypeToken<Any?>() {}.type
        return Gson().fromJson(jsonString, listType)
    }


    @TypeConverter
    fun stringFromListObject(list: List<Any?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun getListObjectFromString(jsonString: String?): List<Any?>? {
        val listType: Type = object : TypeToken<ArrayList<Any?>?>() {}.type
        return Gson().fromJson(jsonString, listType)
    }
}