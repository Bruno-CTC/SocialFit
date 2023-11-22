package com.example.socialfit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialfit.data.ItemAdapter
import com.example.socialfit.data.UserData

class HomeActivity : AppCompatActivity() {
    var username: String? = null
    var data : UserData? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        username = intent.getStringExtra("username")

        Utils.getUserData(this, username!!, { userData ->
            data = userData
            val name = findViewById<TextView>(R.id.tvNome)
            name.text = "Olá, " + userData.name

            val msg = findViewById<TextView>(R.id.txtMensagem)
            Utils.getUserTrainings(this, username!!, { trainings ->
                if (trainings.size == 0) {
                    msg.text = "Você ainda não possui treinos"
                } else {
                    if (data!!.treinoSelecionado == -1)
                        msg.text = "Você ainda não selecionou um treino"
                    else
                        Utils.getTrainingExercises(this, data!!.username, data!!.treinoSelecionado, Utils.currentDay(), { exercises ->
                            if (exercises.size == 0) {
                                msg.text = "Você não possui exercícios para ${Utils.currentDay()}"
                            } else {
                                msg.text = "Seu treino de ${Utils.currentDay()} é: "

                                val recyclerView = findViewById<RecyclerView>(R.id.viewExercicios)
                                val adapter = ItemAdapter(exercises)
                                recyclerView.adapter = adapter
                                recyclerView.layoutManager = LinearLayoutManager(this)
                            }
                        }, { error ->
                            Utils.showDialog(this, "Error", error)
                        })
                }
            }, { error ->
                Utils.showDialog(this, "Error", error)
            })
        }, { error ->
            Utils.showDialog(this, "Error", error)
        })

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
        navigationView.menu.getItem(0).isChecked = true
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    drawerLayout.closeDrawers()
                    menuItem.isChecked = true
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
                    val intent = Intent(this, SearchActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
            true
        }
    }
}