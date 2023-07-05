package ru.netology.myapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity: AppCompatActivity(R.layout.activity_app) {

    @Inject
    lateinit var firebaseMessaging: FirebaseMessaging

    @Inject
    lateinit var googleApiAvailability: GoogleApiAvailability
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        firebaseMessaging.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                println("some stuff happened: ${task.exception}")
                return@addOnCompleteListener
            }

            val token = task.result
            println(token)
        }
        fun checkGoogleApiAvailability() {
            with(googleApiAvailability) {
                val code = isGooglePlayServicesAvailable(this@AppActivity)
                if (code == ConnectionResult.SUCCESS) {
                    return@with
                }
                if (isUserResolvableError(code)) {
                    getErrorDialog(this@AppActivity, code, 9000)?.show()
                    return
                }
                Toast.makeText(
                    this@AppActivity,
                    R.string.google_play_unavailable,
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }
}
