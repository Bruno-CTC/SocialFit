package com.example.socialfit

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button

interface DialogSelectTrainingCallback {
    fun onPositiveButtonClicked(selection: Int)
    fun onNegativeButtonClicked()
}
class DialogSelectTraining(context: Context, private val callback: DialogSelectTrainingCallback) : Dialog(context) {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_select_training)

            val opt1 = findViewById<Button>(R.id.iniciante)
            val opt2 = findViewById<Button>(R.id.intermediario)
            val opt3 = findViewById<Button>(R.id.avancado1)
            val opt4 = findViewById<Button>(R.id.avancado2)
            val opt5 = findViewById<Button>(R.id.avancado3)
            val cancel = findViewById<Button>(R.id.cancelar)

            opt1.setOnClickListener {
                callback.onPositiveButtonClicked(0)
                dismiss()
            }

            opt2.setOnClickListener {
                callback.onPositiveButtonClicked(1)
                dismiss()
            }

            opt3.setOnClickListener {
                callback.onPositiveButtonClicked(2)
                dismiss()
            }

            opt4.setOnClickListener {
                callback.onPositiveButtonClicked(3)
                dismiss()
            }

            opt5.setOnClickListener {
                callback.onPositiveButtonClicked(4)
                dismiss()
            }

            cancel.setOnClickListener {
                callback.onNegativeButtonClicked()
                dismiss()
            }
        }
}