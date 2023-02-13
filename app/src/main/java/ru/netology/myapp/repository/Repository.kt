package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.myapp.dto.Post
import ru.netology.myapp.exceptions.PostNotFoundException
import ru.netology.myapp.viewmodel.newPostId

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Int)
    fun shareById(id:Int)
    fun removeById(id:Int)
    fun save(post: Post)
    fun findPost(id: Int): Post
    fun filterPost(id: Int): List<Post>
}
class PostRepositoryInMemory: PostRepository {
    private var posts = listOf(Post(
            id = 0,
            autor = "Autor_Name. Длииииииииииииииииииииииииииииииииииииинно",
            autorAvatar = "@tools:sample/avatars",
            published = "30 июля в 10.30",
            content = "короткий текст" +
                    "короткий текст",
            url = "www.url.ru",
            likes = 0,
            shares = 999,
            viewes = 22
    ),
        Post(
            id = 1,
            autor = "Name",
            autorAvatar = "@tools:sample/avatars",
            published = "30 июля в 10.30",
            content = "When a view's measure() method returns, its getMeasuredWidth() and getMeasuredHeight() values must be set, along with those for all of that view's descendants. A view's measured width and measured height values must respect the constraints imposed by the view's parents. This guarantees that at the end of the measure pass, all parents accept all of their children's measurements. A parent view may call measure() more than once on its children. For example, the parent may measure each child once with unspecified dimensions to find out how big they want to be, then call measure() on them again with actual numbers if the sum of all the children's unconstrained sizes is too big or too small.",
            url = "www.url.ru",
            likes = 0,
            shares = 91,
            viewes = 212
        ),
        Post(
            id = 2,
            autor = "Name",
            autorAvatar = "@tools:sample/avatars",
            published = "30 июля в 10.30",
            content = "222   When a view's measure() method returns, its getMeasuredWidth() and getMeasuredHeight() values must be set, along with those for all of that view's descendants. A view's measured width and measured height values must respect the constraints imposed by the view's parents. This guarantees that at the end of the measure pass, all parents accept all of their children's measurements. A parent view may call measure() more than once on its children. For example, the parent may measure each child once with unspecified dimensions to find out how big they want to be, then call measure() on them again with actual numbers if the sum of all the children's unconstrained sizes is too big or too small. Kotlin (Ко́тлин) — статически типизированный, объектно-ориентированный язык программирования, работающий поверх Java Virtual Machine и разрабатываемый компанией JetBrains. Также компилируется в JavaScript и в исполняемый код ряда платформ через инфраструктуру LLVM. Язык назван в честь острова Котлин в Финском заливе, на котором расположен город Кронштадт[5].\n\nАвторы ставили целью создать язык более лаконичный и типобезопасный, чем Java, и более простой, чем Scala[5]. Следствием упрощения по сравнению со Scala стали также более быстрая компиляция и лучшая поддержка языка в IDE[6]. Язык полностью совместим с Java, что позволяет Java-разработчикам постепенно перейти к его использованию; в частности, язык также встраивается Android, что позволяет для существующего Android-приложения внедрять новые функции на Kotlin без переписывания приложения целиком. Язык разрабатывается с 2010 года под руководством Андрея Бреслава[7] , представлен общественности в июле 2011[8]. Исходный код реализации языка открыт в феврале 2012[9]. В феврале выпущен milestone 1, включающий плагин для IDEA. В июне — milestone 2 с поддержкой Android[10]. В декабре 2012 года вышел milestone 4, включающий, в частности, поддержку Java 7[11].\n\nВ феврале 2016 года вышел официальный релиз-кандидат версии 1.0[12], а 15 февраля 2016 года — релиз 1.0[13]. 1 марта 2017 вышел релиз 1.1[14].\n\nВ мае 2017 года компания Google сообщила, что инструменты языка Kotlin, основанные на JetBrains IDE, будут по стандарту включены в Android Studio 3.0 — официальный инструмент разработки для ОС Android[15].\n\nНа Google I/O 2019 было объявлено, что язык программирования Kotlin стал приоритетным в разработке под Android[16].\n\nВ ноябре 2020 года Бреслав объявил об уходе из JetBrains, руководство разработкой языка было передано Роману Елизарову[17].Синтаксис языка преимущественно комбинирует наследство из двух языковых ветвей: Cи/C++/Java и ML (по словам создателей, через Scala). Из наиболее характерных элементов, от первой ветви унаследованы блоки кода, обрамлённые фигурными скобками; а от второй — постфиксное указание типов переменных и параметров (сперва идентификатор, затем разделитель — двоеточие, и затем тип) и ключевые слова «fun» и «val». Точка с запятой как разделитель операторов необязательна (как в Scala, Groovy и JavaScript); в большинстве случаев перевода строки достаточно, чтобы компилятор понял, что выражение закончилось. Кроме объектно-ориентированного подхода, Kotlin также поддерживает процедурный стиль с использованием функций. Как и в Си, C++ и D, точка входа в программу — функция main, принимающая массив параметров командной строки. Программы на Kotlin также поддерживают perl- и shell-стиль интерполяции строк (переменные, включённые в строку, заменяются на своё содержимое). Также поддерживается вывод типов.",
            url = "www.url.ru",
            likes = 5,
            shares = 0,
            viewes = 200
        )
    )
    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Int) {
        posts=posts.map {
            if (it.id!=id) it else it.copy(iLiked = !it.iLiked,
        likes = if(it.iLiked) it.likes-1 else it.likes+1)
        }
        data.value=posts
    }

    override fun shareById(id: Int) {
        posts=posts.map {
            if (it.id!=id) it else it.copy(shares=it.shares+1)
        }
        data.value=posts
    }

    override fun removeById(id: Int) {
        posts=posts.filter {it.id!=id}
        data.value=posts
    }

    fun getNextId() = posts.size

    override fun save(post: Post) {

        if    (post.id == newPostId) {
            posts=listOf(post.copy(id = getNextId())) + posts
        } else {
        posts=posts.map {
            if (it.id==post.id) it.copy(content = post.content) else it
        }
        }
        data.value = posts
    }

    override fun findPost(id: Int): Post {
        val pst = posts.find {it.id==id}

        if (pst != null) {
                return pst
            }
            else {throw PostNotFoundException("Пост не найден")}
    }

    override fun filterPost(id: Int): List<Post>
    = posts.filter {it.id==id}

}
