package ru.netology.myapp.viewmodel

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import ru.netology.myapp.R

class AuthDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return requireContext().let {
            val builder = AlertDialog.Builder(it)
            builder
                .setMessage("Доступно только авторизованным пользователям")
                .setIcon(R.drawable.baseline_priority_high_24)
                .setCancelable(true)
                .setPositiveButton("Войти") { _, _ ->
                    findNavController().navigate(R.id.action_dialog_to_signInFragment)
                }
                .setNegativeButton("Отмена") {
                    dialog, id ->  dialog.cancel()
                }
            builder.create()
        }
    }
}


