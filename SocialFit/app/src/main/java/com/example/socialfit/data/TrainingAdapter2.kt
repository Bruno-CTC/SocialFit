package com.example.socialfit.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.socialfit.R
import com.example.socialfit.Utils

class TrainingAdapter2(var items: List<TrainingData>) : RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>(){
    var username: String? = null
    var otherUsername: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingAdapter.TrainingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_training, parent, false)
        return TrainingAdapter.TrainingViewHolder(view)
    }

    fun addTraining(training: TrainingData) {
        val newItems = items.toMutableList()
        newItems.add(training)
        training.id = newItems.size - 1
        items = newItems.toList()
        notifyItemChanged(items.size - 1)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TrainingAdapter.TrainingViewHolder, position: Int) {
        val item = items[position]
        holder.btnTraining.text = item.name
        holder.btnTraining.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Treino")
                .setMessage("Deseja copiar o treino ${item.name}?")
                .setPositiveButton("Sim") { dialog, which ->
                    Utils.copyTraining(holder.itemView.context, otherUsername!!, username!!, item.id, { training ->
                        Utils.showDialog(holder.itemView.context, "Sucesso", "Treino copiado com sucesso")
                    }) { error ->
                        Utils.showDialog(holder.itemView.context, "Erro", error)
                    }
                }
                .setNegativeButton("Não") { dialog, which ->
                    // do nothing
                }
                .show()
        }
    }
}