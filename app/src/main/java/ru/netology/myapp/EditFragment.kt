package ru.netology.myapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import ru.netology.myapp.databinding.FragmentEditBinding
import ru.netology.myapp.dto.AttachmentType
import ru.netology.myapp.viewmodel.AttachmentModel
import ru.netology.myapp.viewmodel.PostViewModel

class EditFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentEditBinding.inflate(
            inflater,
            container,
            false
        )

        val viewModel: PostViewModel by activityViewModels()

        arguments?.textArg?.let {
            binding.editGroup.visibility= View.VISIBLE
            binding.content.setText(it)
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
                    viewModel.changeAttachment(AttachmentModel(uri, file, type))
                }
            }
        }

        val videoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    Activity.RESULT_CANCELED -> {
                        Toast.makeText(requireContext(), "ошибка загрузки", Toast.LENGTH_LONG)
                            .show()
                    }
                    Activity.RESULT_OK -> {
                        val uri = it.data?.data ?: return@registerForActivityResult
                        val file = uri.toFile()
                        val type = AttachmentType.VIDEO
                        viewModel.changeAttachment(AttachmentModel(uri, file, type))
                    }
                }
            }
//        добавляем видео
        binding.addVideo.setOnClickListener {
            val intent = Intent()
                .setType("video/*")
                .setAction(Intent.ACTION_GET_CONTENT)
            videoLauncher.launch(intent)
        }

// аудио добавляем аналогично

        val audioLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    Activity.RESULT_CANCELED -> {
                        Toast.makeText(requireContext(), "ошибка загрузки", Toast.LENGTH_LONG)
                            .show()
                    }

                    Activity.RESULT_OK -> {
                        val uri = it.data?.data ?: return@registerForActivityResult
                        val file = uri.toFile()
                        val type = AttachmentType.VIDEO
                        viewModel.changeAttachment(AttachmentModel(uri, file, type))
                    }
                }
            }

        binding.addVideo.setOnClickListener {
            val intent = Intent()
                .setType("video/*")
                .setAction(Intent.ACTION_GET_CONTENT)
            audioLauncher.launch(intent)
        }

        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.loadPosts()
            findNavController().navigateUp()
        }

        binding.cancel.setOnClickListener {
            viewModel.cancelEdit()
            findNavController().navigateUp()
        }

        requireActivity().addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.new_post_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.save -> {
                        if (binding.content.text.isNullOrBlank()) {
                            Toast.makeText(binding.root.context, getString(R.string.empty_post), Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            viewModel.editContent(binding.content.text.toString())
                            viewModel.save()
                        }
                        true }
                    else -> { false  }
                }
        }, viewLifecycleOwner)
// добавляем фото через лаунчер
        binding.takePhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .cameraOnly()
                .compress(2048)
                .createIntent(photoLauncher::launch)
        }

        binding.pickPhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .galleryOnly()
                .compress(2048)
                .createIntent(photoLauncher::launch)
        }

        viewModel.attachmentState.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.attachmentContainer.isVisible = false
                return@observe
            } else {
                binding.attachmentContainer.isVisible = true
                when (viewModel.attachmentState.value?.type) {
                    AttachmentType.IMAGE -> {
                        binding.photoPreview.isVisible = true
                        binding.videoGroup.isVisible = false
                        binding.audioGroup.isVisible = false
                    }

                    AttachmentType.VIDEO -> {
                        binding.photoPreview.isVisible = false
                        binding.videoGroup.isVisible = true
                        binding.audioGroup.isVisible = false
                    }

                    AttachmentType.AUDIO -> {
                        binding.photoPreview.isVisible = false
                        binding.videoGroup.isVisible = false
                        binding.audioGroup.isVisible = true
                    }

                    else -> {
                        println(viewModel.attachmentState.value?.type.toString())
                    }
                }
            }

            binding.attachmentContainer.isVisible = true
            binding.photoPreview.setImageURI(it.uri)
        }

        binding.removePhoto.setOnClickListener {
            viewModel.changeAttachment(null)
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
