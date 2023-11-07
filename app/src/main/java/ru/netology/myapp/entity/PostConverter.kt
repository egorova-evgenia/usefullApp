package ru.netology.myapp.entity

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.myapp.dto.Coordinates
import ru.netology.myapp.dto.UserPreview

class PostConverter {

    @TypeConverter
    fun fromListInt(list: List<Int>): String {
        return if (list.isNotEmpty()) {
            list.joinToString(",")
        } else {
            ""
        }
    }

    @TypeConverter
    fun toListInt(data: String): List<Int> {
        return if (data.length > 0) {
            listOf(*data.split(",").map { it.toInt() }.toTypedArray())
        } else {
            listOf<Int>()
        }
    }

    private val gson = Gson()
    private val setType = object : TypeToken<Map<Int, UserPreview>>() {}.type

    @TypeConverter
    fun fromMapToString(map: Map<Int, UserPreview>): String = gson.toJson(map)

    @TypeConverter
    fun fromStringToMap(json: String) = gson.fromJson<Map<Int, UserPreview>>(json, setType)

    @TypeConverter
    fun fromCoodrs(coords: Coordinates?): String? {
        return if (coords != null) {
            coords.lat.toString() + "," + coords.long.toString()
        } else {
            null
        }
    }

    @TypeConverter
    fun fromStringToCoodrs(coordString: String?): Coordinates? {
        val res = if (coordString != null) {
            val strgArr = coordString.split(",")
            Coordinates(strgArr[0].toDouble(), strgArr[1].toDouble())
        } else {
            null
        }
        return if (res is Coordinates) {
            res
        } else {
            null
        }
    }


//    @TypeConverter
//    fun toJson(set: List<Int>?): String?{//set=data
//        return if (set!=null) {
//            gson.toJson(set, setType)
//        } else {
//            null
//        }
//    }
//    @TypeConverter
//    fun fromJson(json: String?): List<Int>?{
//        return if (json!=null) {
//             gson.fromJson(json, setType)
//        } else {
//            null
//        }
//    }

}
//https://stackoverflow.com/questions/75567439/room-generic-typeconverter
//https://stackoverflow.com/questions/73670899/room-typeconverter-listint

// java
//https://stackoverflow.com/questions/18544133/parsing-json-array-into-java-util-list-with-gson


//@TypeConverter
//fun fromListInt(list: List<Int>?): String? {
////        list.isNotEmpty()
//    return if (list!=null) {
//        list.joinToString(",")
//    } else {
//        null
//    }
//}
//
//@TypeConverter
//fun toListInt(data: String?): List<Int>? {
//    val res = if (data?.length!! >0) {
//        listOf(*data.split(",").map { it.toInt() }.toTypedArray())
////            java.lang.NumberFormatException: For input string: ""
//    } else {
//        null
//    }
//    return if (res is List<Int>) {
//        res
//    } else {
//        null
//    }
//}