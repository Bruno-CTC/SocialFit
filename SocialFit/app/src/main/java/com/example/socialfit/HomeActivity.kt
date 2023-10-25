package com.example.socialfit

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class HomeActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
        private const val BASE_URL = "http://192.168.168.97:3000" // Replace with your API base URL
    }

    var username: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        // as extra info you get the username from the login
        username = intent.getStringExtra("username")
        // chance the txtUsername to the user's name (database), use volley
        val txtUsername = findViewById<TextView>(R.id.txtUsername)
        val queue: RequestQueue = Volley.newRequestQueue(this)

        // get data for that user
        val jsonObjectRequest = JsonObjectRequest( // get the user's name from the database
            Request.Method.GET, "$BASE_URL/user/$username", null,
            { response ->
                try {
                    txtUsername.text = response.getString("name")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) { error -> error.printStackTrace() }
        queue.add(jsonObjectRequest)

        // logout button (btnLogout)
        val btnLogout = findViewById<TextView>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}