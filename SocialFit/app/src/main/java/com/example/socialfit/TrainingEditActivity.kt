package com.example.socialfit

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class TrainingEditActivity : AppCompatActivity() {
    var username: String? = null
    var trainingId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_training)

        username = intent.getStringExtra("username")
        trainingId = intent.getIntExtra("trainingId", 0)

        val back = findViewById<ImageView>(R.id.btnVoltar)
        back.setOnClickListener {
            val intent = android.content.Intent(this, TrainListActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val delete = findViewById<ImageView>(R.id.btnDeletar)
        delete.setOnClickListener {
            Utils.deleteTraining(this, username!!, trainingId, {
                val intent = android.content.Intent(this, TrainListActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }, { error ->
                Utils.showDialog(this, "Error", error)
            })
        }

        fun editDay(day: String) {
            val intent = android.content.Intent(this, ExerciseListActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("trainingId", trainingId)
            intent.putExtra("day", day)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        val btnSeg = findViewById<Button>(R.id.btnSeg)
        btnSeg.setOnClickListener {
            editDay("Segunda")
        }
        val btnTer = findViewById<Button>(R.id.btnTer)
        btnTer.setOnClickListener {
            editDay("Terca")
        }
        val btnQua = findViewById<Button>(R.id.btnQua)
        btnQua.setOnClickListener {
            editDay("Quarta")
        }
        val btnQui = findViewById<Button>(R.id.btnQui)
        btnQui.setOnClickListener {
            editDay("Quinta")
        }
        val btnSex = findViewById<Button>(R.id.btnSex)
        btnSex.setOnClickListener {
            editDay("Sexta")
        }
        val btnSab = findViewById<Button>(R.id.btnSab)
        btnSab.setOnClickListener {
            editDay("Sabado")
        }
        val btnDom = findViewById<Button>(R.id.btnDom)
        btnDom.setOnClickListener {
            editDay("Domingo")
        }

        val cbSelecionado = findViewById<CheckBox>(R.id.cbSelecionado)
        Utils.getUserData(this, username!!, { user ->
            cbSelecionado.isChecked = user.treinoSelecionado == trainingId
        }, { error ->
            Utils.showDialog(this, "Error", error)
        })
        cbSelecionado.setOnClickListener {
            Utils.getUserData(this, username!!, { user ->
                user.treinoSelecionado = if (cbSelecionado.isChecked) trainingId else -1
                cbSelecionado.isEnabled = false
                Utils.putUserData(this, user, {
                    cbSelecionado.isEnabled = true
                }, { error ->
                    Utils.showDialog(this, "Error", error)
                })
            }, { error ->
                Utils.showDialog(this, "Error", error)
            })
        }

    }
}