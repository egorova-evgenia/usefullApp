package ru.netology.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import ru.netology.myapp.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.content.requestFocus()
        binding.save.setOnClickListener{

            if (binding.content.text.isNullOrBlank()) {
                Toast.makeText( it.context,getString(R.string.empty_post), Toast.LENGTH_SHORT)
                    .show()
                setResult(RESULT_CANCELED)
            } else {

                val result = Intent().putExtra(Intent.EXTRA_TEXT,binding.content.text.toString())
                setResult(RESULT_OK,result)
                }
                    finish()
            }


    }

}