package com.example.pjiii.interfaces.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pjiii.interfaces.BaterPontoActivity
import com.example.pjiii.interfaces.MontarAgendaActivity
import com.example.pjiii.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.example.pjiii.data.MontarAgenda
import com.example.pjiii.interfaces.adapter.showBottomSheet
import com.example.pjiii.interfaces.adapter.validError

class LoginActivity : AppCompatActivity(){

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var referece: DatabaseReference
    var controler = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding =  ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        referece = Firebase.database.reference

       // var dadostrans = mutableListOf<Dados1>()
        //

        initListeners()

    }

    private fun initListeners(){
        binding.criarConta.setOnClickListener{
            criarConta()
        }

        binding.btnLogin.setOnClickListener {
            validaDados()
        }
    }

    private fun validaDados(){
        val user = binding.User.text.toString().trim()
        val senha = binding.Senha.text.toString().trim()

        if (user.isNotEmpty()) {

            if (senha.isNotEmpty()) {
                loginUser(user,senha)
            } else {
                showBottomSheet("Atenção", "Entendi", "Senha Invalida")
            }
        }
        else{
            showBottomSheet("Atenção", "Entendi", "Usuario invalido")
        }
    }

    private fun loginUser(email: String,password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                  //  verificação da agenda se ja montada
                    verificaAgenda()
                    //startActivity(Intent(this, MainActivityP::class.java))
                } else {
                    showBottomSheet("Atenção", "Entendi", message = validError(task.exception?.message ?: ""))
                }
            }
    }

    private fun verificaAgenda() {
        var c = 0
        referece
            .child("Dias")
            .child(auth.currentUser?.uid?:"")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var dadosDI = mutableListOf<MontarAgenda>()
                    for (ds in snapshot.children) {
                        val dia = ds.getValue(MontarAgenda::class.java) as MontarAgenda
                        dadosDI.add(dia)
                    }
                    if (!(dadosDI.isEmpty())&& c==0) {
                        c=1
                        proximaAct()
                        //onDestroy()
                    } else {
                        c=1
                        proximaActAge()
                        //onDestroy()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
                }
            })


    }

    private fun criarConta(){
        startActivity(Intent(this, CriarContaActivity::class.java))
    }

    private fun proximaAct() {
        startActivity(Intent(this, BaterPontoActivity::class.java))
    }

    private fun proximaActAge() {
        startActivity(Intent(this, MontarAgendaActivity::class.java))
    }

}