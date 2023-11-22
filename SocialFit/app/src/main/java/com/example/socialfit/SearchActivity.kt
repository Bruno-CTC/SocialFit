package com.example.socialfit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.socialfit.data.UserData

class SearchActivity : AppCompatActivity() {
    var username: String? = null
    var data : UserData? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        username = intent.getStringExtra("username")

        val rec = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvPesquisa)
        val adapter = com.example.socialfit.data.UserAdapter(listOf())
        adapter.username = username
        rec.adapter = adapter
        rec.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        val txtSearch = findViewById<android.widget.EditText>(R.id.txtPesquisa)

        // instead of on press enter i want it to search every time the text changes
        txtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                val search = txtSearch.text.toString()
                if (search != "") {
                    Utils.searchForUser(this@SearchActivity, search, { users ->
                        // remove the item from the list if it is the same as the user
                        val filteredUsers = users.filter { user -> user.username != username }
                        adapter.items = filteredUsers
                        adapter.notifyDataSetChanged()
                    }) { error ->
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // do nothing
            }
        })


        val sidebar = findViewById<ImageView>(R.id.btnSidebar)
        sidebar.setOnClickListener{
            val drawerLayout = findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawerLayout)
            drawerLayout.open()
        }


        val navigationView = findViewById<com.google.android.material.navigation.NavigationView>(R.id.navigationView)
        val drawerLayout = findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawerLayout)

        val toggle= ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // set the home button to checked
        navigationView.menu.getItem(2).isChecked = true
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    try {
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.putExtra("username", username)
                        startActivity(intent)
                        drawerLayout.closeDrawers()
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                    catch (e: Exception) {
                        Utils.showDialog(this, "Error", e.message.toString())
                    }
                }
                R.id.nav_training_list -> {
                    try {
                        val intent = Intent(this, TrainListActivity::class.java)
                        intent.putExtra("username", username)
                        startActivity(intent)
                        drawerLayout.closeDrawers()
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                    catch (e: Exception) {
                        Utils.showDialog(this, "Error", e.message.toString())
                    }
                }
                R.id.nav_logout -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    Utils.removeVariable(this, "username")
                    startActivity(intent)
                }
                R.id.nav_search -> {
                    drawerLayout.closeDrawers()
                    menuItem.isChecked = true
                }
            }
            true
        }
    }
}