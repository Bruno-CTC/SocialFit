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

class TrainListActivity:AppCompatActivity() {
    companion object {
        private const val BASE_URL = "http://192.168.168.97:3000" // Replace with your API base URL
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        var bttnCasa2 = findViewById<ImageView>(R.id.bttnCasa2)
        bttnCasa2.setOnClickListener{
            val inten= Intent(this,HomeActivity::class.java)
        }

        var bttnPontinho = findViewById<ImageView>(R.id.bttnPontinho)
        bttnPontinho.setOnClickListener{
            val inte= Intent(this,TrainListActivity::class.java)
        }
    }
}