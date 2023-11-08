package com.example.socialfit

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity

class TrainListActivity:AppCompatActivity() {

    var username: String? = null
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_trainings);
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        username = intent.getStringExtra("username")

        Utils.getUserTrainings(this, username!!, { trainings ->
            val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.viewExercicios)
            val adapter = com.example.socialfit.data.TrainingAdapter(trainings)
            adapter.username = username
            recyclerView.adapter = adapter
            recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        }, { error ->
            Utils.showDialog(this, "Error", error)
        })

        val addTrainingButton = findViewById<ImageView>(R.id.btnAdd)
        addTrainingButton.setOnClickListener {
            val dialog = DialogNewTraining(this, object: NewTrainingDialogCallback {
                override fun onPositiveButtonClicked(name: String, description: String) {
                    val data = com.example.socialfit.data.TrainingData()
                    data.name = name
                    data.description = description
                    Utils.postTrainingData(this@TrainListActivity, username!!, data, { res ->
                        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.viewExercicios)
                        val adapter = recyclerView.adapter as com.example.socialfit.data.TrainingAdapter
                        adapter.addTraining(data)
                    }, { error ->
                        Utils.showDialog(this@TrainListActivity, "Error", error)
                    })
                }

                override fun onNegativeButtonClicked() {
                    // do nothing
                }
            })
        dialog.show()

        }

        // Sidebar

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
        navigationView.menu.getItem(1).isChecked = true
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
                R.id.nav_training_list -> {
                    menuItem.isChecked = true
                    drawerLayout.closeDrawers()
                }
                R.id.nav_logout -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    Utils.removeVariable(this, "username")
                    startActivity(intent)
                }
            }
            true
        }
    }
}