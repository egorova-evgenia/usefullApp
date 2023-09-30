package ru.netology.myapp.entity

import androidx.room.TypeConverter

class PostConverter {

    @TypeConverter
    fun fromListInt(list: List<Int>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toListInt(data: String): List<Int> {
        return listOf(*data.split(",").map { it.toInt() }.toTypedArray())
    }

}

//https://stackoverflow.com/questions/73670899/room-typeconverter-listint