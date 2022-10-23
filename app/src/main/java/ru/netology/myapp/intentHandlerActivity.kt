//package ru.netology.myapp
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.Toast
//import com.google.android.material.snackbar.Snackbar
//import ru.netology.myapp.databinding.ActivityIntentHandlerBinding
//
//class intentHandlerActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_intent_handler)
//        val binding = ActivityIntentHandlerBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.send.setOnClickListener(){
//            val intent= Intent()
//                .putExtra(Intent.EXTRA_TEXT, "hard text")
//                .setAction(Intent.ACTION_SEND)
//                .setType("text/plain")
//            val createChooser = Intent.createChooser(intent, "Choose app")
//        }
//
//        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
//            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
//        } ?: run {
//            Snackbar.make(binding.root,"Content es empty",Toast.LENGTH_SHORT)
//                .setAction(android.R.string.ok){
//                    finish()
//                }.show()
//
//        }
//    }
//}