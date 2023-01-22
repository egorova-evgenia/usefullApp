package ru.netology.myapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.myapp.databinding.FragmentOnePostBinding
import ru.netology.myapp.viewmodel.PostViewModel

class OnePostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOnePostBinding.inflate(
            inflater,
            container,
            false
        )
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
//        arguments?.textArg?.let {
//            binding.content.setText(it)
//
//        }

        val arg1Value = requireArguments().getString("text")
        binding.content.setText(arg1Value)
        val arg2Value = requireArguments().getInt("autor")
        binding.autor.setText(arg2Value)



        return binding.root
    }
    companion object {
        var Bundle.textArg: String? by StringArg
    }
}