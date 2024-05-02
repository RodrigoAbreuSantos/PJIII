package com.example.pjiii.interfaces.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pjiii.databinding.ActivityCriarContaBinding
import com.example.pjiii.interfaces.adapter.showBottomSheet
import com.example.pjiii.interfaces.adapter.validError
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


class CriarContaActivity : AppCompatActivity(){

    private lateinit var binding: ActivityCriarContaBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var referece: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCriarContaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        referece = Firebase.database.reference

        initListeners()

    }

    private fun initListeners(){
        binding.txtLogin.setOnClickListener{
            back()
        }

        binding.btnCriarConta.setOnClickListener {
            conferirCadastro()
        }
    }

    private fun conferirCadastro() {
        val user = binding.NovoUser.text.toString().trim()
        val senha = binding.NovaSenha.text.toString().trim()

        if (user.isNotEmpty()) {

            if (senha.isNotEmpty()) {
                registerUser(user,senha)
            } else {
                showBottomSheet("Atenção", "Entendi", "Senha Invalida")
            }
        }
        else{
            showBottomSheet("Atenção", "Entendi", "Usuario invalido")
        }

    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    back()
                    //onDestroy()
                } else {
                    showBottomSheet("Atenção", "Entendi", message = validError(task.exception?.message ?: ""))
                }
            }
    }

    private fun back() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    }

