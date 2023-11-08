package com.example.socialfit

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ExerciseEditActivity : AppCompatActivity() {
    var username: String? = null
    var trainingId = 0
    var day: String? = null
    var exerciseId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_exercise)

        username = intent.getStringExtra("username")
        trainingId = intent.getIntExtra("trainingId", 0)
        day = intent.getStringExtra("day")
        exerciseId = intent.getIntExtra("exerciseId", 0)

        val back = findViewById<ImageView>(R.id.btnVoltar)
        back.setOnClickListener {
            val intent = android.content.Intent(this, ExerciseListActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("trainingId", trainingId)
            intent.putExtra("day", day)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val delete = findViewById<ImageView>(R.id.btnDeletar)
        delete.setOnClickListener {
            Utils.deleteExercise(this, username!!, trainingId, day!!, exerciseId, {
                val intent = android.content.Intent(this, ExerciseListActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("trainingId", trainingId)
                intent.putExtra("day", day)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }, { error ->
                Utils.showDialog(this, "Error", error)
            })
        }

        Utils.getExerciseData(this, username!!, trainingId, day!!, exerciseId, { exercise ->
            val name = findViewById<android.widget.TextView>(R.id.txtDados)
            name.text = "Nome: " + exercise.name + "\n\n" +
                    "Descrição: " + exercise.description + "\n\n" +
                    "Repetições: " + exercise.repetitions + "\n\n" +
                    "Séries: " + exercise.series + "\n\n" +
                    "Descanso: " + exercise.rest + " segundos"
        }, { error ->
            Utils.showDialog(this, "Error", error)
        })
    }
}