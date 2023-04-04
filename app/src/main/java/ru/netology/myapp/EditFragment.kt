package ru.netology.myapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.myapp.databinding.FragmentEditBinding
import ru.netology.myapp.repository.utils.AndroidUtils
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

        arguments?.textArg?.let {
            binding.editGroup.visibility= View.VISIBLE
            binding.content.setText(it)
        }

        binding.save.setOnClickListener {
            viewModel.editContent(binding.content.text.toString())
            viewModel.save()
//            AndroidUtils.hideKeyboard((requireView()))
        }
        viewModel.postCreated.observe(viewLifecycleOwner){
            viewModel.loadPosts()
            findNavController().navigateUp()
        }

        binding.cancel.setOnClickListener {
            viewModel.cancelEdit()
            findNavController().navigateUp()
        }
        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }
}
