package com.example.socialfit

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity



class ProfileActivity:AppCompatActivity() {
    companion object {
        private const val BASE_URL = "http://192.168.168.97:3000" // Replace with your API base URL
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val bHome = findViewById<ImageView>(R.id.bttnHomesinha)
        bHome.setOnClickListener{
            val inte = Intent(this,HomeActivity::class.java)
            startActivity(inte)
        }

        val bPonto = findViewById<ImageView>(R.id.bttnTres)
        bHome.setOnClickListener{
            val inten = Intent(this,HomeActivity::class.java)
            startActivity(inten)
        }




    }

}