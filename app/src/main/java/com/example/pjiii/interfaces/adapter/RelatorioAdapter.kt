package com.example.pjiii.interfaces.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pjiii.data.BaterPontoHorario
import com.example.pjiii.databinding.RelatorioAdapterBinding

class RelatorioAdapter(
    //Vamos exibir a lista de informações que queremos
    private val relatorioList: List<BaterPontoHorario>
): RecyclerView.Adapter<RelatorioAdapter.MyViewHolder>() {

    //Para criar a vizualização para cada relatorio
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //estamos setando nossa vizualização para nossa classe interna MyViewHolder
        return MyViewHolder(RelatorioAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    //Onde de fato vamos exibir cada informação dinamicamente, pela posição do item na lista
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //tipo de informação que estamos listando no adapter
        val relatorio = relatorioList[position] //se a lista tiver 10 itens, vai chamar esse metodo 10 vezes

        holder.binding.txtDia.text = relatorio.dia
    }

    //Vamos setar o tamanho da nossa lista,
    // e o recycler view ja sabe o tamanho dela para poder fazer a listagem
    override fun getItemCount() = relatorioList.size //vai retornar o tamanho da nossa lista

    //Referencias dos nossos atributos no nosso layout
    //inner class é uma classe interna
    inner class MyViewHolder(val binding: RelatorioAdapterBinding): RecyclerView.ViewHolder(binding.root){

    }

}