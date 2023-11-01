package com.example.socialfit

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class TrainActivity:AppCompatActivity() {
    companion object {
        private const val BASE_URL = "http://192.168.168.97:3000" // Replace with your API base URL
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val btHome = findViewById<ImageView>(R.id.bttnHom)
        btHome.setOnClickListener{
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }

        val btPonto = findViewById<ImageView>(R.id.bttnTres)
        btPonto.setOnClickListener{
            val inten = Intent(this,HomeActivity::class.java)
            startActivity(inten)
        }
        val bttnAdicionar = findViewById<ImageView>(R.id.bttnAdEx)
        bttnAdicionar.setOnClickListener{
            //aqui devemos adicionar o treino desejado no perfil do usuario
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }


        val bttnDeletar = findViewById<ImageView>(R.id.bttnDeletar)
        bttnDeletar.setOnClickListener{
            // aqui devemos deletar algum exercício do treino
            val inten = Intent(this,ProfileActivity::class.java)
            startActivity(inten)
        }

        val btVoltar = findViewById<ImageView>(R.id.bttnVoltar)
        btVoltar.setOnClickListener{
            val inte = Intent(this, TrainActivity::class.java)
            startActivity(inte)
        }

    }

}