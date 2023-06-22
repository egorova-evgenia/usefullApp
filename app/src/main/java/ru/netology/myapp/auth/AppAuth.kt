package ru.netology.myapp.auth

import android.content.Context
import androidx.core.content.edit
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.netology.myapp.ServerService.Api
import ru.netology.myapp.fcm.PushToken

class AppAuth private constructor(context: Context){
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
//    private val idKey ="id"
//    private val tokenKey ="token"

    private val _authStateFlow = MutableStateFlow(AuthState())
    val authStateFlow = _authStateFlow.asStateFlow()

    init {
        var id:Long =0L
        var token:String? = null

        token = prefs.getString(TOKEN, null)
        id = prefs.getLong(ID, 0L)

        if(id ==0L || token == null){
            _authStateFlow.value = AuthState()
            prefs.edit{
                clear()
            }
        } else {
            _authStateFlow.value = AuthState(id =id,token = token)
        }
        sendPushToken()
    }

    @Synchronized
    fun clear(){
        _authStateFlow.value = AuthState()
        prefs.edit{
            clear()
        }
        sendPushToken()
    }

    @Synchronized
    fun setAuth(id: Long, token: String){
        _authStateFlow.value = AuthState(id =id,token = token)
        prefs.edit {
            putLong(ID,id)
            putString(TOKEN, token)
        }
        sendPushToken()
    }


    fun sendPushToken(token: String? = null) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val pushToken = PushToken(token ?: FirebaseMessaging.getInstance().token.await())
//                Api.service.saveToken(pushToken)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
    }

    companion object {
        const val ID= "id"
        const val TOKEN= "token"

        @Volatile
        private var INSTANCE:AppAuth? = null

        fun init(context: Context){
            synchronized(this){
                INSTANCE = AppAuth(context)
            }
        }

        fun getInstance(): AppAuth =
            synchronized(this) {
                requireNotNull(INSTANCE)
            }
    }

}
data class AuthState(val id: Long = 0, val token: String? = null)
