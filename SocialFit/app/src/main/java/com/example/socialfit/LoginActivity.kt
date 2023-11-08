package com.example.socialfit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_login)

        val button = findViewById<Button>(R.id.btnIrCadastrar)
        button.setOnClickListener {
            try {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            } catch (e: Exception) {
                Utils.showDialog(this, "Error", e.message)
            }
        }

        val login = findViewById<Button>(R.id.btnLogin)
        login.setOnClickListener {
            try {
                val username = findViewById<android.widget.EditText>(R.id.tvUsuario).text.toString().trim()
                val password = findViewById<android.widget.EditText>(R.id.tvSenha).text.toString().trim()

                if (username.isEmpty() || password.isEmpty()) {
                    Utils.showDialog(this, "Error", "Username and password must not be empty")
                }

                Utils.getUserData(this, username, { userData ->
                    if (userData.password == password) {
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.putExtra("username", username)
                        Utils.saveVariable(this, "username", username)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
                    } else {
                        Utils.showDialog(this, "Error", "Wrong password")
                    }
                }, { error ->
                    Utils.showDialog(this, "Error", "User not found")
                })
            } catch (e: Exception) {
                Utils.showDialog(this, "Error", e.message)
            }
        }
    }
}
