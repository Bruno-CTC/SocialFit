package com.example.socialfit

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText

interface ExerciseDialogCallback {
    fun onPositiveButtonClicked(name: String, description: String, rest: String, repetitions: String, series: String)
    fun onNegativeButtonClicked()
}
class DialogNewExercise(context: Context, private val callback: ExerciseDialogCallback) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_new_exercise)

        val okButton = findViewById<Button>(R.id.okButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val restEditText = findViewById<EditText>(R.id.restEditText)
        val repetitionsEditText = findViewById<EditText>(R.id.repetitionsEditText)
        val seriesEditText = findViewById<EditText>(R.id.seriesEditText)

        okButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val rest = restEditText.text.toString()
            val repetitions = repetitionsEditText.text.toString()
            val series = seriesEditText.text.toString()
            callback.onPositiveButtonClicked(name, description, rest, repetitions, series)
            dismiss()
        }

        cancelButton.setOnClickListener {
            callback.onNegativeButtonClicked()
            dismiss()
        }
    }
}