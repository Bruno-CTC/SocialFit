package com.example.socialfit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity



class ProfileActivity:AppCompatActivity() {
    var username: String? = null
    var otherUsername : String? = null
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        username = intent.getStringExtra("username")
        otherUsername = intent.getStringExtra("otherUsername")

        var back = findViewById<android.widget.ImageView>(R.id.btnVoltar);
        back.setOnClickListener{
            finish();
        }

        var rcTreinos = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rcTreinos);
        var adapter = com.example.socialfit.data.TrainingAdapter2(listOf());
        adapter.username = username;
        adapter.otherUsername = otherUsername;
        rcTreinos.adapter = adapter;
        rcTreinos.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this);

        var txtUsername = findViewById<android.widget.TextView>(R.id.txtUsuario)
        Utils.getUserData(this, otherUsername!!, { user ->
            "${user.name} (@${user.username})".also { txtUsername.text = it }
        }) { error ->
            Utils.showDialog(this, "Erro", error)
        }

        Utils.getUserTrainings(this, otherUsername!!, { trainings ->
            adapter.items = trainings
            adapter.notifyDataSetChanged()
        }) { error ->
            Utils.showDialog(this, "Erro", error)
        }
    }

}