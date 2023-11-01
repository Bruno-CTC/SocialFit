package com.example.socialfit

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



class TrainActivity:AppCompatActivity() {
    companion object {
        private const val BASE_URL = "http://192.168.168.97:3000" // Replace with your API base URL
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val btHome = findViewById<ImageView>(R.id.bttnHomesinha)
        btHome.setOnClickListener{
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(in)
        }

        val btPonto = findViewById<ImageView>(R.id.Tres)
        btPonto.setOnClickListener{
            val inten = Intent(this,HomeActivity::class.java)
            startActivity(inten)
        }
        val bttnAdicionar = findViewById<ImageView>(R.id.bttnAdicionar)
        bttnAdicionar.setOnClickListener{
            //aqui devemos adicionar o treino desejado no perfil do usuario
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }

        val bttnModificar = findViewById<ImageView>(R.id.bttnAdEx)
        bttnAdEx.setOnClickListener{
            //aqui devemos mudar de tela para que seja adicionado um novo exercicio ao treino
            val intent = Intent(this,ExerciseActivity::class.java)
            startActivity(intent)
        }

        val bttnDeletar = findViewById<ImageView>(R.id.BttnDeletar)
        bttnDeletar.setOnClickListener{
            // aqui devemos deletar algum exercício do treino
            val inten = Intent(this,ProfileActivity::class.java)
            startActivity(inten)
        }

        val btVoltar = findViewById<ImageView>(R.id.bttnVoltar)
        btVoltar.setOnClickListener{
            val inte = Intent(this, TrainList::class.java)
            startActivity(inte)
        }

    }

}