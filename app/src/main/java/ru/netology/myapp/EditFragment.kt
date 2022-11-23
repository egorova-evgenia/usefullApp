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
import ru.netology.myapp.viewmodel.PostViewModel

class EditFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val binding = FragmentEditBinding.inflate(
            inflater,
            container,
            false
        )

        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        arguments?.textArg?.let(binding.content::setText)

        val txt=binding.content
        println(txt)

//        if (intent.getStringExtra(Intent.EXTRA_TEXT)!=null) {
//            val text = intent.getStringExtra(Intent.EXTRA_TEXT).toString()
//            binding.editGroup.visibility= View.VISIBLE
//            binding.content.setText(text)
//        } else {
//            binding.editGroup.visibility= View.GONE
//        }

        binding.save.setOnClickListener {

            if (binding.content.text.isNullOrBlank()) {
                Toast.makeText(it.context, getString(R.string.empty_post), Toast.LENGTH_SHORT)
                    .show()
//                activity?.setResult(Activity.RESULT_CANCELED)
            } else {

                viewModel.editContent(binding.content.text.toString())
                viewModel.save()
            }

            findNavController().navigateUp()
        }

        binding.cancel.setOnClickListener {
            viewModel.cancelEdit()
//            activity?.setResult(Activity.RESULT_CANCELED)
            findNavController().navigateUp()
        }
        return binding.root

    }

//    companion object {
//        var Bundle.textArg: String? StringArg
//    }

    companion object {
        private const val CONTENT_KEY = "CONTENT_KEY"
        var Bundle.textArg: String?
            set(value) = putString(CONTENT_KEY,textArg)
            get() = getString(CONTENT_KEY)
    }
}
