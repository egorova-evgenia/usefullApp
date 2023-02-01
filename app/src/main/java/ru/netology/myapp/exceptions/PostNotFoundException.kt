package ru.netology.myapp.exceptions
import java.lang.RuntimeException

class PostNotFoundException(message: String) : RuntimeException(message)