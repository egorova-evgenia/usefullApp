package ru.netology.myapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.netology.myapp.databinding.FragmentNewPostBinding

class NewPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        super.onCreate(savedInstanceState)
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )
        binding.save.setOnClickListener{
//            val intent =Intent()
            if (binding.content.text.isNullOrBlank()) {
                Toast.makeText( it.context,getString(R.string.empty_post), Toast.LENGTH_SHORT)
                    .show()
                activity?.setResult(Activity.RESULT_CANCELED)
            } else {

                val result = Intent().putExtra(Intent.EXTRA_TEXT,binding.content.text.toString())
                activity?.setResult(Activity.RESULT_OK,result)
                }
                    findNavController().navigateUp()
            }

        return binding.root
    }

}