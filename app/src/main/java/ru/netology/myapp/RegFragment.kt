package ru.netology.myapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.myapp.databinding.FragmentSignInBinding
import androidx.navigation.fragment.findNavController
import ru.netology.myapp.databinding.FragmentRegBinding
import ru.netology.myapp.viewmodel.AuthViewModel

class RegFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRegBinding.inflate(
            inflater,
            container,
            false
        )

        val viewModel: AuthViewModel by viewModels(ownerProducer = ::requireParentFragment)

        binding.okButton.setOnClickListener {
            val  login = binding.putLogin.text.trim().toString()
            val password = binding.putPassword.text.trim().toString()
            // to do login,password отправить на сервер?????



            if (binding.putLogin.text.isNullOrBlank()||binding.putPassword.text.isNullOrBlank()) {
                val message = if (binding.putLogin.text.isNullOrBlank()) "введите логин" else "введите пароль"
                Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT)
                    .show()
            } else {
                val  login = binding.putLogin.text.trim().toString()
                val password = binding.putPassword.text.trim().toString()
//                viewModel.autorization(login,password)//вьюмодель для
                 findNavController().navigateUp()
            }

        }




        binding.toSignIn.setOnClickListener{
//            go to the reg fragment
//            findNavController()
        }
        return binding.root

    }

}