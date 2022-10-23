package ru.netology.myapp

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class EditModeActivityContract: ActivityResultContract<String, String?>() {
    override fun createIntent(context: Context, input: String): Intent =
        Intent(context, NewPostActivity::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?): String? =
        intent?.getStringExtra(Intent.EXTRA_TEXT)
}