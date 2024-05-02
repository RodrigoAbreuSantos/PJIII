package com.example.pjiii.interfaces

import com.example.pjiii.validações.Geolocalização
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pjiii.databinding.ActivityBaterPontoBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.example.pjiii.data.MontarAgenda
import com.example.pjiii.data.BaterPontoHorario
import com.example.pjiii.interfaces.adapter.showBottomSheet
import java.util.Calendar


class BaterPontoActivity : AppCompatActivity() {

    private lateinit var binding:  ActivityBaterPontoBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var referece: DatabaseReference
    var controleTrueFalse = false
   //lateinit var diaAtual = calendar.get(Calendar.DAY_OF_WEEK)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBaterPontoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        referece = Firebase.database.reference

        entradaOuSaida2()

        initListeners()

    }

    private fun initListeners(){
        binding.btbPonto.setOnClickListener{
            val geolocalização = Geolocalização(this)
            geolocalização.fecthLocation(this){isInPucCampinas ->
                if(isInPucCampinas){
                    baterPonto()
                }
                else showBottomSheet("Atenção", "Entendi", "Não está em PUC-CAMP")
                //else Toast.makeText(this, "Não está em PUC-CAMP", Toast.LENGTH_SHORT).show()

            }
        }

        binding.BtnRelatorio.setOnClickListener {
            startActivity(Intent(this, RelatorioActivity::class.java))
        }
    }

    private fun baterPonto(){
        getUser()
    }

    private fun getUser(){
        referece
            .child("Dias")
            .child(auth.currentUser?.uid?:"")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var dadosDI = mutableListOf<MontarAgenda>()
                    var contador = -1
                    var controle = false
                    val diaHoje = diaDeHoje()
                    for (ds in snapshot.children) {
                        val dia = ds.getValue(MontarAgenda::class.java) as MontarAgenda
                        dadosDI.add(dia)
                        contador+=1

                    }
                    while(contador!=-1){
                        if(diaHoje == dadosDI[contador].dia.toString()){
                            controle = true
                        }
                        contador = contador - 1
                    }
                    if(controle) getHora()
                    else{
                        showBottomSheet("Atenção", "Entendi", "Não pode bater ponto")
                        //Toast.makeText(applicationContext, "Não pode bater ponto", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun diaDeHoje(): String {
        val calendario = Calendar.getInstance()
        val diaAtual = calendario.get(Calendar.DAY_OF_WEEK)
        if (diaAtual == 1) return "Domingo"
        if (diaAtual == 2) return "Segunda-Feira"
        if (diaAtual == 3) return "Terça-Feira"
        if (diaAtual == 4) return "Quarta-Feira"
        if (diaAtual == 5) return "Quinta-Feira"
        if (diaAtual == 6) return "Sexta-Feira"
        if (diaAtual == 7) return "Sábado"
        return TODO("Provide the return value")
    }

    private fun getHora(){
        referece
            .child("Dias")
            .child(auth.currentUser?.uid?:"")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var dadosDI = mutableListOf<MontarAgenda>()
                    var contador = -1
                    var calendario = Calendar.getInstance()
                    var hrAtual = calendario.get(Calendar.HOUR_OF_DAY)
                    var controle = false
                    for (ds in snapshot.children) {
                        val dia = ds.getValue(MontarAgenda::class.java) as MontarAgenda
                        dadosDI.add(dia)
                        contador+=1

                    }
                    while(contador!=-1){
                        if(hrAtual >= dadosDI[contador].hre && hrAtual <= dadosDI[contador].hrs){
                            controle = true
                        }
                        contador = contador - 1
                    }
                    if(controle) entradaOuSaida()
                    else{
                        showBottomSheet("Atenção", "Entendi", "Não pode bater ponto")
                        //Toast.makeText(applicationContext, "Não pode bater ponto", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun salvarPontoEntrada(){
        val idDad = referece.database.reference.push().key ?: ""
        var calendario = Calendar.getInstance()
        var Ponto = BaterPontoHorario()
        val diaHoje = diaDeHoje()
        Ponto.OutOrIn = "Entrada"
        Ponto.hr = calendario.get(Calendar.HOUR_OF_DAY)
        Ponto.Mins = calendario.get(Calendar.MINUTE)
        Ponto.dia = diaHoje
        referece
            .child("Ponto")
            .child(auth.currentUser?.uid?:"")
            .child(idDad)
            .setValue(Ponto)
        //showBottomSheet("Sucesso", "Entendi", "Sucesso ao registrar")
        Toast.makeText(this, "Sucesso ao registrar", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, BaterPontoActivity::class.java))
    }

    private fun entradaOuSaida() {
        var controle =0
        referece
            .child("Ponto")
            .child(auth.currentUser?.uid?:"")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var horario = mutableListOf<BaterPontoHorario>()
                    for (ds in snapshot.children) {
                        val hora = ds.getValue(BaterPontoHorario::class.java) as BaterPontoHorario
                        horario.add(hora)
                    }
                    if (horario.isEmpty() && controle==0) {
                        salvarPontoEntrada()
                        controle+=1
                    }
                    if(!(horario.isEmpty())&& controle ==0) {
                        verificação()
                        controle += 1
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun verificação(){
        var contador = -1
        var verificador = 0
        referece
            .child("Ponto")
            .child(auth.currentUser?.uid?:"")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var horario = mutableListOf<BaterPontoHorario>()
                    for (ds in snapshot.children) {
                        val hora = ds.getValue(BaterPontoHorario::class.java) as BaterPontoHorario
                        horario.add(hora)
                        contador+=1
                    }
                    if(horario[contador].OutOrIn=="Saída" && verificador==0) {
                        salvarPontoEntrada()
                        verificador+=1
                    }
                    if(horario[contador].OutOrIn=="Entrada" && verificador==0){
                        salvarPontoSaida()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun salvarPontoSaida(){
        val idDad = referece.database.reference.push().key ?: ""
        var calendario = Calendar.getInstance()
        var Ponto = BaterPontoHorario()
        val diaHoje = diaDeHoje()
        Ponto.OutOrIn = "Saída"
        Ponto.hr = calendario.get(Calendar.HOUR_OF_DAY)
        Ponto.Mins = calendario.get(Calendar.MINUTE)
        Ponto.dia = diaHoje
        referece
            .child("Ponto")
            .child(auth.currentUser?.uid?:"")
            .child(idDad)
            .setValue(Ponto)
        //showBottomSheet("Sucesso", "Entendi", "Sucesso ao registrar")
        Toast.makeText(this, "Sucesso ao registrar", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, BaterPontoActivity::class.java))
    }

    private fun entradaOuSaida2() {
        var controle =0
        referece
            .child("Ponto")
            .child(auth.currentUser?.uid?:"")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var horario = mutableListOf<BaterPontoHorario>()
                    for (ds in snapshot.children) {
                        val hora = ds.getValue(BaterPontoHorario::class.java) as BaterPontoHorario
                        horario.add(hora)
                    }
                    if (horario.isEmpty() && controle==0) {
                        binding.textView.text = "Seu próximo ponto é uma entrada!"
                        controle+=1
                    }
                    if(!(horario.isEmpty())&& controle ==0) {
                        verificação2()
                        controle += 1
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun verificação2(){
        var contador = -1
        var verificador = 0
        referece
            .child("Ponto")
            .child(auth.currentUser?.uid?:"")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var horario = mutableListOf<BaterPontoHorario>()
                    for (ds in snapshot.children) {
                        val hora = ds.getValue(BaterPontoHorario::class.java) as BaterPontoHorario
                        horario.add(hora)
                        contador+=1
                    }
                    if(horario[contador].OutOrIn=="Saída" && verificador==0) {
                        binding.textView.text = "Seu próximo ponto é uma entrada!"
                        verificador+=1
                    }
                    if(horario[contador].OutOrIn=="Entrada" && verificador==0){
                        binding.textView.text = "Seu próximo ponto é uma saída!"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                }

            })
    }
}