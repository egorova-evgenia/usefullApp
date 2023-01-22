//package ru.netology.myapp
//import android.os.Bundle
//import ru.netology.myapp.dto.Post
//import kotlin.properties.ReadWriteProperty
//import kotlin.reflect.KProperty
//
//object PostArg : ReadWriteProperty<Bundle, Post?> {
//    override fun getValue(thisRef: Bundle, property: KProperty<*>): Post? =
//        thisRef.getPost(property.name)
//
//    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: String?) {
//        thisRef.putString(property.name,value)
//    }
//}