package ru.netology.myapp.auth

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.netology.myapp.ServerService.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val idKey = "ID"
    private val tokenKey = "TOKEN"
    private val _authStateFlow = MutableStateFlow(AuthState())
    val authStateFlow = _authStateFlow.asStateFlow()

    init {
        var id: Int = 0
        var token: String? = null
        token = prefs.getString(tokenKey, null)
        id = prefs.getInt(idKey, 0)

        if (id == 0 || token == null) {
            _authStateFlow.value = AuthState()
            prefs.edit {
                clear()
            }
        } else {
            _authStateFlow.value = AuthState(id = id, token = token)
        }
        sendPushToken()
    }

    @Synchronized
    fun clear() {
        _authStateFlow.value = AuthState()
        prefs.edit {
            clear()
        }
        sendPushToken()
    }

    @Synchronized
    fun setAuth(id: Int, token: String) {
        _authStateFlow.value = AuthState(id = id, token = token)
        prefs.edit {
            putInt(idKey, id)
            putString(tokenKey, token)
        }
        sendPushToken()
    }


    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface AppAuthEntryPoint {
        fun apiService(): ApiService
    }
    fun sendPushToken(token: String? = null) {
        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val pushToken = PushToken(token ?: FirebaseMessaging.getInstance().token.await())
//                val entryPoint =
//                    EntryPointAccessors.fromApplication(context, AppAuthEntryPoint::class.java)
//                getApiService(context).saveToken(pushToken)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
        }
    }

    private fun getApiService(context: Context): ApiService {
        val hiltEntryPoint =
            EntryPointAccessors.fromApplication(context, AppAuthEntryPoint::class.java)
        return hiltEntryPoint.apiService()
    }
}

data class AuthState(val id: Int = 0, val token: String? = null)
