package com.example.socialfit;

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
        private const val BASE_URL = "http://192.168.15.102:3000" // Replace with your API base URL
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_login)

        val button = findViewById<Button>(R.id.btnCadastrarLogin)
        button.setOnClickListener {
            try {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            } catch (e: Exception) {
                showErrorDialog("Error", e.message)
            }
        }

        val login = findViewById<Button>(R.id.btnLogin)
        login.setOnClickListener {
            try {
                makeLoginRequest(
                    findViewById<android.widget.EditText>(R.id.tvUsuarioLogin).text.toString(),
                    findViewById<android.widget.EditText>(R.id.tvSenhaLogin).text.toString()
                )
            } catch (e: Exception) {
                showErrorDialog("Error", e.message)
            }
        }
    }

    private fun makeLoginRequest(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            showErrorDialog("Error", "Username and password must not be empty")
            return
        }

        val queue: RequestQueue = Volley.newRequestQueue(this)
        val url = "$BASE_URL/user/$username" // Adjust to your login endpoint

        // get data for that user
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val user = response.getString("password")
                    if (user == password) {
                        showErrorDialog("Success", "Login successful")
                    } else {
                        showErrorDialog("Error", "Incorrect password")
                    }
                } catch (e: JSONException) {
                    showErrorDialog("Error", e.message)
                }
            },
            { error ->
                if (error.networkResponse.statusCode == 404) {
                    showErrorDialog("Error", "User not found")
                } else {
                    showErrorDialog("Error", error.message)
                }
            }
        )

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    private fun showErrorDialog(title : String, message: String?) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message ?: "Unknown error")
            .setPositiveButton("OK", null)
            .show()
    }
}
