package ru.netology.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import ru.netology.myapp.databinding.ActivityEditBinding
import ru.netology.myapp.databinding.ActivityNewPostBinding
import ru.netology.myapp.repository.utils.AndroidUtils

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getStringExtra(Intent.EXTRA_TEXT)!=null) {
            val text = intent.getStringExtra(Intent.EXTRA_TEXT).toString()
            binding.editGroup.visibility= View.VISIBLE
            binding.content.setText(text)
        }

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

        binding.cancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

    }
}