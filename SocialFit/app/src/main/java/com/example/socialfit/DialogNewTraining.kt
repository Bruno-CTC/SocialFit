package com.example.socialfit

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText

interface NewTrainingDialogCallback {
    fun onPositiveButtonClicked(name: String, description: String)
    fun onNegativeButtonClicked()
}
class DialogNewTraining(context: Context, private val callback: NewTrainingDialogCallback) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_new_training)

        val okButton = findViewById<Button>(R.id.okButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)

        okButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            callback.onPositiveButtonClicked(name, description)
            dismiss()
        }

        cancelButton.setOnClickListener {
            callback.onNegativeButtonClicked()
            dismiss()
        }
    }
}