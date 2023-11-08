package com.example.socialfit.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socialfit.R

class ItemAdapter(private val items: List<ExerciseData>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_exercise, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item.name
        holder.descriptionView.text = "Séries: ${item.series}\nRepetições: ${item.repetitions}\nDescanso: ${item.rest} segundos\nDescrição: ${item.description}"

        var isExpanded = false
        holder.expandButton.setOnClickListener {
            isExpanded = !isExpanded
            if (isExpanded) {
                holder.descriptionView.visibility = View.VISIBLE
                holder.expandButton.text = "Collapse"
                holder.descriptionView.alpha = 0.0f
                holder.descriptionView.animate()
                    .alpha(1.0f)
                    .setDuration(300)
                    .setListener(null)
            } else {
                holder.descriptionView.animate()
                    .alpha(0.0f)
                    .setDuration(300)
                    .withEndAction { holder.descriptionView.visibility = View.GONE }
                holder.expandButton.text = "Expand"

            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val descriptionView: TextView = itemView.findViewById(R.id.descriptionView)
        val expandButton: Button = itemView.findViewById(R.id.expandButton)
    }
}