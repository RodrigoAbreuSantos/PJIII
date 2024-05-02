package com.example.pjiii.interfaces

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pjiii.data.BaterPontoHorario
import com.example.pjiii.databinding.ActivityRelatorioBinding
import com.example.pjiii.interfaces.adapter.RelatorioAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class RelatorioActivity : AppCompatActivity() {

    private lateinit var biding: ActivityRelatorioBinding

    //Adapter
    private lateinit var relatorioAdapter: RelatorioAdapter

    //Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var referece: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        biding = ActivityRelatorioBinding.inflate(layoutInflater)
        setContentView(biding.root)

        auth = Firebase.auth
        referece = Firebase.database.reference

        initListeners()

    }

    private fun initListeners(){
        initRecyclerView(getTasks())
        biding.progressBar.isVisible = false
    }

    private fun initRecyclerView(relatorioList: List<BaterPontoHorario>){

        relatorioAdapter = RelatorioAdapter(relatorioList)

        biding.rvTask.layoutManager = LinearLayoutManager(this) //vamos realizar a exibição de forma linear, ou seja um componente abaixo do outro
        biding.rvTask.setHasFixedSize(true)//para dar uma performance melhor na nossa listagem
        biding.rvTask.adapter = relatorioAdapter//vamos passar o adapter que criamos

    }

    private fun getDados(){//metodo que vamos buscar as informações no firebase
        referece
            .child("Ponto") //esta percorrendo o nó
            .child(auth.currentUser?.uid?:"")//esta percorrendo o nó do id do user, dentro do nó Ponto
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) { //qualquer modificação que vc tenha, esse Snapshot é notificado
                    val relatorioList = mutableListOf<BaterPontoHorario>()
                    for (ds in snapshot.children){ //recupera todas as informações
                        val relatorio = ds.getValue(BaterPontoHorario::class.java) as BaterPontoHorario
                        relatorioList.add(relatorio) //esta adicionando todas as informações na lista
                    }
                    initRecyclerView(relatorioList)
                }

                override fun onCancelled(error: DatabaseError) {//quando faz a busca, mas no meio vc fecha o app
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                }

            })
    }
    //addListener --> vai fazer uma consulta no firebase e vai trazer as informações,
// se depois disso qualquer informação for alterada no banco de dados ele não faz mais nada, porque ele só é executado quando o usuario abre o app
    //addValue --> fica monitorando toda e qualquer alteração no Firebase

    private fun getTasks() = listOf<BaterPontoHorario>(
        BaterPontoHorario("Quarta", "Entrada", 18, 57),
        BaterPontoHorario("Quinta", "Entrada", 18, 57),
        BaterPontoHorario("Sexta", "Entrada", 18, 57),
        BaterPontoHorario("Sabado", "Entrada", 18, 57),
        BaterPontoHorario("Quarta", "Entrada", 18, 57),
        BaterPontoHorario("Quinta", "Entrada", 18, 57),
        BaterPontoHorario("Sexta", "Entrada", 18, 57),
        BaterPontoHorario("Quarta", "Entrada", 18, 57),
        BaterPontoHorario("Quinta", "Entrada", 18, 57),
        BaterPontoHorario("Sexta", "Entrada", 18, 57)
    )

}