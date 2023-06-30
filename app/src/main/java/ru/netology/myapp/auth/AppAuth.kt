package ru.netology.myapp.auth

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.netology.myapp.ServerService.ApiService
import ru.netology.myapp.service.PushToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    @ApplicationContext
    private val context: Context){
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val idKey ="ID"
    private val tokenKey ="TOKEN"

//        const val ID= "id"
//        const val TOKEN= "token"

    private val _authStateFlow = MutableStateFlow(AuthState())
    val authStateFlow = _authStateFlow.asStateFlow()

    init {
        var id:Long =0L
        var token:String? = null



        token = prefs.getString(tokenKey, null)
        id = prefs.getLong(idKey, 0L)

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
            putLong(idKey,id)
            putString(tokenKey, token)
        }
        sendPushToken()
    }


    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface AppAuthEntryPoint {
        fun getApiService(): ApiService
    }


    fun sendPushToken(token: String? = null) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val pushToken = PushToken(token ?: FirebaseMessaging.getInstance().token.await())
//        EntryPointAccessors.fromApplication(context, AppAuthEntryPoint::class.java)
//                Api.service.saveToken(pushToken)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
        }
    }

//    companion object {
//        const val ID= "id"
//        const val TOKEN= "token"
//
//        @Volatile
//        private var INSTANCE:AppAuth? = null
//
//        fun init(context: Context){
//            synchronized(this){
//                INSTANCE = AppAuth(context)
//            }
//        }
//
//        fun getInstance(): AppAuth =
//            synchronized(this) {
//                requireNotNull(INSTANCE)
//            }
//    }


data class AuthState(val id: Long = 0, val token: String? = null)
