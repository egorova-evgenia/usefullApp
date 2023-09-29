package ru.netology.myapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import ru.netology.myapp.databinding.FragmentSignInBinding
import androidx.navigation.fragment.findNavController
import ru.netology.myapp.viewmodel.AuthViewModel
import ru.netology.myapp.viewmodel.PostViewModel
import ru.netology.myapp.viewmodel.SignInViewModel


class SignInFragment  : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignInBinding.inflate(
            inflater,
            container,
            false
        )

        val viewModel: SignInViewModel by activityViewModels()

        binding.okButton.setOnClickListener {
            if (binding.putLogin.text.isNullOrBlank()||binding.putPassword.text.isNullOrBlank()) {
                val message = if (binding.putLogin.text.isNullOrBlank()) "введите логин" else "введите пароль"
                Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT)
                    .show()
            } else {
                val login = binding.putLogin.text.trim().toString()
                val password = binding.putPassword.text.trim().toString()
                println("trim password, login")
                viewModel.updateUser(login, password)//вьюмодель для
                findNavController().navigateUp()
            }

//            todo
//            В SignInFragment хотелось бы, чтобы сначала приходил результат,
//            а потом была навигация. Сейчас наоборот.
//            Вы можете добиться такого эффекта при помощи SingleLiveEvent

        }




        binding.toReg.setOnClickListener{
//            go to the reg fragment
//            findNavController()
        }
        return binding.root

    }

}