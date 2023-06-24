package kz.mobydev.drevmass.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type
import kz.mobydev.drevmass.model.Lesson


class RoomDataConverter : Serializable {

    @TypeConverter
    fun stringFromObject(list: Lesson?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun getObjectFromString(jsonString: String?): Lesson? {
        val listType: Type = object : TypeToken<Lesson?>() {}.type
        return Gson().fromJson(jsonString, listType)
    }


    @TypeConverter
    fun stringFromListObject(list: List<Lesson?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun getListObjectFromString(jsonString: String?): List<Lesson?>? {
        val listType: Type = object : TypeToken<ArrayList<Lesson?>?>() {}.type
        return Gson().fromJson(jsonString, listType)
    }
}