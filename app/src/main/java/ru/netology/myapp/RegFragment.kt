package ru.netology.myapp

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import ru.netology.myapp.databinding.FragmentRegBinding
import ru.netology.myapp.dto.AttachmentType
import ru.netology.myapp.viewmodel.AttachmentForSaving
import ru.netology.myapp.viewmodel.RegViewModel

class RegFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRegBinding.inflate(
            inflater,
            container,
            false
        )

        val viewModel: RegViewModel by activityViewModels()

        binding.okButton.setOnClickListener {
            if (binding.putLogin.text.isNullOrBlank() || binding.putPassword.text.isNullOrBlank()) {
                Toast.makeText(
                    binding.root.context,
                    "Введите все необходимые данные",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                val  login = binding.putLogin.text.trim().toString()
                val password = binding.putPassword.text.trim().toString()
                val name = binding.putName.text.trim().toString()
                viewModel.registerUser(login,password,name)
                findNavController().navigateUp()
            }

        }

        val photoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            when(it.resultCode){
                ImagePicker.RESULT_ERROR ->{
                    Toast.makeText(requireContext(),"ошибка загрузки", Toast.LENGTH_LONG)
                        .show()
                }
                Activity.RESULT_OK -> {
                    val uri = it.data?.data ?: return@registerForActivityResult
                    val file = uri.toFile()
                    val type = AttachmentType.IMAGE
                    viewModel.changePhoto(AttachmentForSaving(uri, file, type))
                }
            }
        }

        binding.pickAvatar.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .galleryOnly()
                .compress(512)
                .createIntent(photoLauncher::launch)
        }

        binding.takeAvatar.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .cameraOnly()
                .compress(2048)
                .createIntent(photoLauncher::launch)
        }

        binding.toSignIn.setOnClickListener{
//            go to the reg fragment
//            findNavController()
        }
        return binding.root

    }

}