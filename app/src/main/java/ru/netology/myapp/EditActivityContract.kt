package ru.netology.myapp

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class EditActivityContract: ActivityResultContract<String, String?>() {
    override fun createIntent(context: Context, input: String): Intent =
        Intent(context, EditActivity::class.java).putExtra(Intent.EXTRA_TEXT, input)
    override fun parseResult(resultCode: Int, intent: Intent?): String? =
        intent?.getStringExtra(Intent.EXTRA_TEXT)
//    override fun parseResult(resultCode: Int, intent: Intent?): String? =
//        if (resultCode == Activity.RESULT_OK) {
//            intent?.getStringExtra(Intent.EXTRA_TEXT)
//        } else {
//            null
//        }
}


