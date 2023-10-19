package com.example.socialfit;

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        val button = findViewById<Button>(R.id.btnCadastrar);
        button.setOnClickListener {
            try {
                val 
            } catch (e: Exception) {
                AlertDialog.Builder(this)
                    .setTitle("Erro")
                    .setMessage(e.message)
                    .setPositiveButton("OK", null)
                    .show()
            }
        }

        val button2 = findViewById<Button>(R.id.btnLoginCadastrar);
        button2.setOnClickListener {
            try {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            } catch (e: Exception) {
                AlertDialog.Builder(this)
                    .setTitle("Erro")
                    .setMessage(e.message)
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }
}