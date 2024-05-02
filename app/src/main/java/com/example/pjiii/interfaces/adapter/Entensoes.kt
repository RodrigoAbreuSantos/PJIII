package com.example.pjiii.interfaces.adapter

import android.app.Activity
import com.example.pjiii.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Activity.showBottomSheet(
    titleDialog: String? = null,
    titleButton: String? = null,
    message: String,
    onClick: () -> Unit = {}
) {
    val bottomSheetDialog = BottomSheetDialog(this)
    val bottomSheetBinding: BottomSheetBinding =
        BottomSheetBinding.inflate(layoutInflater, null, false)

    bottomSheetBinding.textTitle.text = titleDialog
    bottomSheetBinding.textMessage.text = message
    bottomSheetBinding.btnClick.text = titleButton
    bottomSheetBinding.btnClick.setOnClickListener {
        onClick()
        bottomSheetDialog.dismiss()
    }

    bottomSheetDialog.setContentView(bottomSheetBinding.root)
    bottomSheetDialog.show()
}

fun validError(error: String): String {
    return when {
        error.contains("There is no user record corresponding to this identifier") -> {
            "Conta não registrada. Por favor, registre-se primeiro."
        }
        error.contains("The email address is badly formatted") -> {
            "Formato de e-mail inválido."
        }
        error.contains("The password is invalid or the user does not have a password") -> {
            "Senha inválida ou nenhum senha associada a este usuário."
        }
        error.contains("The email address is already in use by another account") -> {
            "Endereço de e-mail já está sendo usado por outra conta."
        }
        error.contains("Password should be at least 6 characters") -> {
            "A senha deve ter pelo menos 6 caracteres."
        }
        else -> {
            "Usuario ou Senha Inválido."
        }
    }
}
