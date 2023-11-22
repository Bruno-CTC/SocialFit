package com.example.socialfit.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.socialfit.R

class TrainingAdapter(var items: List<TrainingData>) : RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>(){
    var username: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_training, parent, false)
        return TrainingViewHolder(view)
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

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        val item = items[position]
        holder.btnTraining.text = item.name
        holder.btnTraining.setOnClickListener {
            val intent = android.content.Intent(holder.itemView.context, com.example.socialfit.TrainingEditActivity::class.java)
            intent.putExtra("trainingId", item.id)
            intent.putExtra("username", username)
            holder.itemView.context.startActivity(intent)
        }
    }

    class TrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnTraining = itemView.findViewById<Button>(R.id.btnTrain)
    }
}