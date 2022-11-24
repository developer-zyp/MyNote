package com.example.mynote

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.mynote.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNav()

    }

    private fun setupBottomNav() {
        navController = Navigation.findNavController(this, R.id.nav_host)

        binding.apply {

            bottomNavigationView.setupWithNavController(navController)

            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                bottomNavigationView.visibility = when (destination.id) {
                    R.id.noteAddFragment -> View.GONE
                    else -> View.VISIBLE

                }
            }

        }

//        NavigationUI.setupActionBarWithNavController(this, navController)

    }

}