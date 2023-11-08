package com.example.socialfit;

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.socialfit.data.UserData

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        val button = findViewById<Button>(R.id.btnCadastrar);
        button.setOnClickListener {
            try {
                val name = findViewById<android.widget.EditText>(R.id.tvNome).text.toString().trim()
                val id = findViewById<android.widget.EditText>(R.id.tvUsuario).text.toString().trim()
                val password = findViewById<android.widget.EditText>(R.id.tvSenha).text.toString().trim()
                val email = findViewById<android.widget.EditText>(R.id.tvEmail).text.toString().trim()
                val phone = findViewById<android.widget.EditText>(R.id.tvTelefone).text.toString().trim()

                if (name.isEmpty() || id.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    AlertDialog.Builder(this)
                        .setTitle("Erro")
                        .setMessage("Todos os campos devem ser preenchidos")
                        .setPositiveButton("OK", null)
                        .show()
                    return@setOnClickListener
                }

                if (id.length < 4) {
                    AlertDialog.Builder(this)
                        .setTitle("Erro")
                        .setMessage("O nome de usuário deve ter entre 4 e 15 caracteres")
                        .setPositiveButton("OK", null)
                        .show()
                    return@setOnClickListener
                }

                if (email.length < 5 || !email.contains('@')) {
                    AlertDialog.Builder(this)
                        .setTitle("Erro")
                        .setMessage("O email deve ter entre 5 e 30 caracteres e conter '@'")
                        .setPositiveButton("OK", null)
                        .show()
                    return@setOnClickListener
                }

                if (name.length < 3) {
                    AlertDialog.Builder(this)
                        .setTitle("Erro")
                        .setMessage("O nome deve ter entre 3 e 30 caracteres")
                        .setPositiveButton("OK", null)
                        .show()
                    return@setOnClickListener
                }

                if (password.length < 8) {
                    AlertDialog.Builder(this)
                        .setTitle("Erro")
                        .setMessage("A senha deve ter entre 8 e 30 caracteres")
                        .setPositiveButton("OK", null)
                        .show()
                    return@setOnClickListener
                }

                if (phone.length < 9) {
                    AlertDialog.Builder(this)
                        .setTitle("Erro")
                        .setMessage("O telefone deve ter entre 9 e 12 caracteres")
                        .setPositiveButton("OK", null)
                        .show()
                    return@setOnClickListener
                }

                val userData = UserData()
                userData.username = id
                userData.name = name
                userData.email = email
                userData.password = password
                userData.phone = phone
                Utils.postUserData(this, userData, {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                }, {
                    AlertDialog.Builder(this)
                        .setTitle("Erro")
                        .setMessage("Usuário já cadastrado")
                        .setPositiveButton("OK", null)
                        .show()
                })
            } catch (e: Exception) {
                AlertDialog.Builder(this)
                    .setTitle("Erro")
                    .setMessage(e.message)
                    .setPositiveButton("OK", null)
                    .show()
            }
        }

        val cadastrar = findViewById<Button>(R.id.btnIrLogin);
        cadastrar.setOnClickListener {
            try {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            } catch (e: Exception) {
                AlertDialog.Builder(this)
                    .setTitle("Erro")
                    .setMessage(e.message)
                    .setPositiveButton("OK", null)
                    .show()
            }
        }

        if (Utils.getVariable(this, "username") != null) {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("username", Utils.getVariable(this, "username"))
            startActivity(intent)
            finish()
        }
    }
}