package com.example.socialfit

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.socialfit.data.ExerciseData

class ExerciseListActivity : AppCompatActivity() {
    var username: String? = null
    var trainingId = 0
    var day: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_exercises)

        username = intent.getStringExtra("username")
        trainingId = intent.getIntExtra("trainingId", 0)
        day = intent.getStringExtra("day")

        val back = findViewById<ImageView>(R.id.btnVoltar)
        back.setOnClickListener {
            val intent = android.content.Intent(this, TrainingEditActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("trainingId", trainingId)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val add = findViewById<ImageView>(R.id.btnAdd)
        add.setOnClickListener {
            val dialog = DialogNewExercise(this, object: ExerciseDialogCallback {
                override fun onPositiveButtonClicked(name: String, description: String, rest: String, repetitions: String, series: String) {
                    if (name.isEmpty() || description.isEmpty() || rest.isEmpty() || repetitions.isEmpty() || series.isEmpty()) {
                        Utils.showDialog(this@ExerciseListActivity, "Error", "Preencha todos os campos")
                        return
                    }
                    if (rest.toInt() <= 0 || repetitions.toInt() <= 0 || series.toInt() <= 0) {
                        Utils.showDialog(this@ExerciseListActivity, "Error", "Valores inválidos")
                        return
                    }
                    if (name.length > 30) {
                        Utils.showDialog(this@ExerciseListActivity, "Error", "Nome muito longo")
                        return
                    }
                    else if (name.length < 4) {
                        Utils.showDialog(this@ExerciseListActivity, "Error", "Nome muito curto")
                        return
                    }
                    if (description.length > 100) {
                        Utils.showDialog(this@ExerciseListActivity, "Error", "Descrição muito longa")
                        return
                    }
                    if (repetitions.toInt() > 100) {
                        Utils.showDialog(this@ExerciseListActivity, "Error", "Repetições muito grande")
                        return
                    }
                    if (series.toInt() > 100) {
                        Utils.showDialog(this@ExerciseListActivity, "Error", "Séries muito grande")
                        return
                    }
                    val data = ExerciseData()
                    data.name = name
                    data.description = description
                    data.rest = rest.toInt()
                    data.repetitions = repetitions.toInt()
                    data.series = series.toInt()
                    Utils.postExerciseData(this@ExerciseListActivity, username!!, trainingId, day!!, data, { res ->
                        try {
                            val recyclerView =
                                findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.viewExercicios)
                            val adapter =
                                recyclerView.adapter as com.example.socialfit.data.ExerciseAdapter2
                            adapter.addExercise(data)
                        } catch (e: Exception) {
                            Utils.showDialog(this@ExerciseListActivity, "Error", e.message)
                        }
                    }, { error ->
                        Utils.showDialog(this@ExerciseListActivity, "Error", error)
                    })
                }

                override fun onNegativeButtonClicked() {
                    // do nothing
                }
            })
            dialog.show()
        }

        Utils.getTrainingExercises(this, username!!, trainingId, day!!, { exercises ->
            val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.viewExercicios)
            val adapter = com.example.socialfit.data.ExerciseAdapter2(exercises)
            adapter.username = username
            adapter.trainingId = trainingId
            adapter.day = day
            recyclerView.adapter = adapter
            recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        }, { error ->
            Utils.showDialog(this, "Error", error)
        })
    }
}