package com.example.socialfit.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialfit.R

class ExerciseAdapter2(private var items: List<ExerciseData>) : RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>(){
    // same view holder due to same layout
    var username: String? = null
    var trainingId = 0
    var day: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingAdapter.TrainingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_training, parent, false)
        return TrainingAdapter.TrainingViewHolder(view)
    }

    fun addExercise(exercise: ExerciseData) {
        val newItems = items.toMutableList()
        newItems.add(exercise)
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
            val intent = android.content.Intent(holder.itemView.context, com.example.socialfit.ExerciseEditActivity::class.java)
            intent.putExtra("trainingId", trainingId)
            intent.putExtra("exerciseId", item.id)
            intent.putExtra("day", day)
            intent.putExtra("username", username)
            holder.itemView.context.startActivity(intent)
        }
    }
}