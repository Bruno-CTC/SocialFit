package com.example.socialfit

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class AdActivity : AppCompatActivity() {

    companion object {
        private const val BASE_URL = "http://192.168.168.97:3000" // Replace with your API base URL
    }

    var username: String? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar);
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        val btnHome = findViewById<ImageView>(R.id.bttnCasaaa)
        btnHome.setOnClickListener{
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }

        val btnPonto = findViewById<ImageView>(R.id.bttnPonto1)
        btnPonto.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val btnAdExercicio = findViewById<Button>( R.id.button6)
        btnAdExercicio.setOnClickListener{
            //aqui deve ser adicionado a um treino o exercício digitado pela pessoa.
        }


    }
}