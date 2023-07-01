package ru.netology.myapp

import android.app.Activity
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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import ru.netology.myapp.databinding.FragmentEditBinding
import ru.netology.myapp.repository.utils.AndroidUtils
import ru.netology.myapp.viewmodel.PhotoModel
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

        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

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
                    val file =uri.toFile()
                    viewModel.changePhoto(PhotoModel(uri, file) )
                }
            }
        }

        viewModel.postCreated.observe(viewLifecycleOwner){
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
//                    R.id.cancel ->{}
                    else -> { false  }
                }
        }, viewLifecycleOwner)

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

        viewModel.postCreated.observe(viewLifecycleOwner){
            viewModel.loadPosts()
            findNavController().navigateUp()
        }

        viewModel.photoState.observe(viewLifecycleOwner){
            if (it == null){
                binding.photoContainer.isVisible=false
                return@observe}
            binding.photoContainer.isVisible=true
            binding.preview.setImageURI(it.uri)
        }

        binding.removePhoto.setOnClickListener {
            viewModel.changePhoto(null)
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
