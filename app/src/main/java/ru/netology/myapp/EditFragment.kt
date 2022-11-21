package ru.netology.myapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.myapp.FeedFragment.Companion.textArg
import ru.netology.myapp.databinding.FragmentEditBinding

class EditFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        super.onCreate(savedInstanceState)
        val binding = FragmentEditBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.textArg?.let (binding.content::setText)
//        {
//            binding.editGroup.visibility= View.VISIBLE
//
//        }

//        if (intent.getStringExtra(Intent.EXTRA_TEXT)!=null) {
//            val text = intent.getStringExtra(Intent.EXTRA_TEXT).toString()
//            binding.editGroup.visibility= View.VISIBLE
//            binding.content.setText(text)
//        } else {
//            binding.editGroup.visibility= View.GONE
//        }

        binding.save.setOnClickListener{

            if (binding.content.text.isNullOrBlank()) {
                Toast.makeText( it.context,getString(R.string.empty_post), Toast.LENGTH_SHORT)
                    .show()
//                activity?.setResult(Activity.RESULT_CANCELED)
            } else {

                val result = Intent().putExtra(Intent.EXTRA_TEXT,binding.content.text.toString())
//                activity?.setResult(Activity.RESULT_OK,result) // Это нужно передавать?
//                viewModel.editContent(result.toString())
//                viewModel.save()
            }

            findNavController().navigateUp()
        }

        binding.cancel.setOnClickListener {
//            viewModel.cancelEdit
//            activity?.setResult(Activity.RESULT_CANCELED)
            findNavController().navigateUp()
        }
        return binding.root

    }
}