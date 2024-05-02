package com.example.pjiii.interfaces

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pjiii.databinding.ActivityMontarAgendaBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.example.pjiii.data.MontarAgenda
import com.example.pjiii.interfaces.adapter.showBottomSheet
import com.example.pjiii.interfaces.adapter.validError


class MontarAgendaActivity : AppCompatActivity(){
    private lateinit var biding: ActivityMontarAgendaBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var refereceP: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        biding = ActivityMontarAgendaBinding.inflate(layoutInflater)
        setContentView(biding.root)

        auth = Firebase.auth
        refereceP = Firebase.database.reference

        var dias = arrayOf("Selecione o dia trabalhado", "Segunda-Feira", "Terça-Feira", "Quarta-Feira"
        , "Quinta-Feira", "Sexta-Feira","Sábado")

        var hre = arrayOf("Selecione hora de entrada", "07:00", "08:00", "09:00","10:00","11:00","12:00"
            , "13:00", "14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00")

        var hrs = arrayOf("Selecione hora de saida", "07:00", "08:00", "09:00","10:00","11:00","12:00"
            , "13:00", "14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00")



        biding.spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dias)
        biding.spinnerHora.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, hre)
        biding.spinnerHoraSaida.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, hrs)

        initListeners()

    }

    private fun initListeners(){
        biding.BtnSave.setOnClickListener {
            salvarData()
        }

        biding.NextBtn.setOnClickListener {
            startActivity(Intent(this, BaterPontoActivity::class.java))
        }
    }

    private fun salvarData() {
        var controler = 0
        var dias = arrayOf("Selecione o dia trabalhado", "Segunda-Feira", "Terça-Feira", "Quarta-Feira"
            , "Quinta-Feira", "Sexta-Feira","Sábado")

        var hre = arrayOf(0, 7 , 8, 9,10,11,12
            , 13, 14,15,16,17,18,19,20, 21)

        var hrs = arrayOf(0, 7 , 8, 9,10,11,12
            , 13, 14,15,16,17,18,19,20, 21)

        var hrEntr = biding.spinnerHora.selectedItemPosition


        var hrSai = biding.spinnerHoraSaida.selectedItemPosition

        var diaSelec = biding.spinner.selectedItemPosition.toString()

        if(hrEntr>hrSai){
            Toast.makeText(this, "Horario e saida invalido", Toast.LENGTH_SHORT).show()
        }
        else {
            if (diaSelec == "Selecione o dia trabalhado" || hrEntr == 0 || hrSai == 0) {
                Toast.makeText(this, "'Selecione o seu... é invalido'", Toast.LENGTH_SHORT).show()
            } else {

                var diasData = MontarAgenda()

                var diaSelec = biding.spinner.selectedItemPosition
                diasData.dia = dias[diaSelec]

                var hrEntr = biding.spinnerHora.selectedItemPosition
                diasData.hre = hre[hrEntr]

                var hrSai = biding.spinnerHoraSaida.selectedItemPosition
                diasData.hrs = hrs[hrSai]

                val idDad = refereceP.database.reference.push().key ?: ""

                if (controler == 0) {
                    refereceP
                        .child("Dias")
                        .child(auth.currentUser?.uid ?: "")
                        .child(idDad)
                        .setValue(diasData)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                showBottomSheet("Sucesso", "Entendi", "Data salva com sucesso")
                            }else{
                                showBottomSheet("Atenção", "Entendi", message = validError(task.exception?.message ?: ""))
                            }
                        }
                    //startActivity(Intent(this, MainActivityP::class.java))
                    controler = 1
                }
            }
        }
    }

}

