package com.example.socialfit;

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    companion object {
        private const val BASE_URL = "http://192.168.168.97:3000" // Replace with your API base URL
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        val button = findViewById<Button>(R.id.btnCadastrar);
        button.setOnClickListener {
            try {
                val name = findViewById<android.widget.EditText>(R.id.tvNome).text.toString()
                val id = findViewById<android.widget.EditText>(R.id.tvUsuario).text.toString()
                val password = findViewById<android.widget.EditText>(R.id.tvSenha).text.toString()
                val email = findViewById<android.widget.EditText>(R.id.tvEmail).text.toString()
                val phone = findViewById<android.widget.EditText>(R.id.tvTelefone).text.toString()

                if (name.isEmpty() || id.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    AlertDialog.Builder(this)
                        .setTitle("Erro")
                        .setMessage("Todos os campos devem ser preenchidos")
                        .setPositiveButton("OK", null)
                        .show()
                    return@setOnClickListener
                }

                if (id.length < 4 || id.length > 15) {
                    AlertDialog.Builder(this)
                        .setTitle("Erro")
                        .setMessage("O nome de usuário deve ter entre 4 e 15 caracteres")
                        .setPositiveButton("OK", null)
                        .show()
                    return@setOnClickListener
                }

                if (email.length < 5 || email.length > 30 || !email.contains('@')) {
                    AlertDialog.Builder(this)
                        .setTitle("Erro")
                        .setMessage("O email deve ter entre 5 e 30 caracteres e conter '@'")
                        .setPositiveButton("OK", null)
                        .show()
                    return@setOnClickListener
                }

                if (name.length < 3 || name.length > 30) {
                    AlertDialog.Builder(this)
                        .setTitle("Erro")
                        .setMessage("O nome deve ter entre 3 e 30 caracteres")
                        .setPositiveButton("OK", null)
                        .show()
                    return@setOnClickListener
                }

                if (password.length < 8 || password.length > 30) {
                    AlertDialog.Builder(this)
                        .setTitle("Erro")
                        .setMessage("A senha deve ter entre 8 e 30 caracteres")
                        .setPositiveButton("OK", null)
                        .show()
                    return@setOnClickListener
                }

                if (phone.length < 9 || phone.length > 12) {
                    AlertDialog.Builder(this)
                        .setTitle("Erro")
                        .setMessage("O telefone deve ter entre 9 e 12 caracteres")
                        .setPositiveButton("OK", null)
                        .show()
                    return@setOnClickListener
                }

                val params = JSONObject()
                params.put("name", name)
                params.put("id", id)
                params.put("password", password)
                params.put("email", email)
                params.put("phone", phone)

                val queue = Volley.newRequestQueue(this)
                val stringRequest = object : StringRequest(Method.POST, "$BASE_URL/user",
                    Response.Listener<String> { response ->
                        try {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
                        } catch (e: Exception) {
                            AlertDialog.Builder(this)
                                .setTitle("Error")
                                .setMessage(e.message)
                                .setPositiveButton("OK", null)
                                .show()
                        }
                    },
                    Response.ErrorListener { error ->
                        AlertDialog.Builder(this)
                            .setTitle("Error")
                            .setMessage("E-mail ou nome de usuário já cadastrados")
                            .setPositiveButton("OK", null)
                            .show()
                    }) {
                    override fun getBodyContentType(): String {
                        return "application/json; charset=utf-8"
                    }

                    override fun getBody(): ByteArray {
                        return params.toString().toByteArray()
                    }
                }

                queue.add(stringRequest)

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

        val btnCasa  = findViewById<Button>(R.id.btnCasa)
        btnCasa.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

    }
}