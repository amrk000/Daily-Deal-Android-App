package com.amrk000.dailydeal.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.amrk000.dailydeal.Model.ItemData
import com.amrk000.dailydeal.Model.Permissions
import com.amrk000.dailydeal.Model.UserData
import com.amrk000.dailydeal.R
import com.amrk000.dailydeal.data.local.AppDatabase
import com.amrk000.dailydeal.databinding.ActivityMainBinding
import com.amrk000.dailydeal.util.ImageBase64Converter
import com.amrk000.dailydeal.util.UserAccountManager
import com.amrk000.dailydeal.viewModel.MainViewModel
import com.google.android.material.navigation.NavigationBarView
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //view binding init
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        //window init
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        //view model init
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //nav controller init
        navController = Navigation.findNavController(this, R.id.fragmentsContainer)

        //Database init default data
        viewModel.initDatabaseData()

        //Bottom Navbar
        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation.setOnItemSelectedListener(object: NavigationBarView.OnItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                return NavigationUI.onNavDestinationSelected(item, navController)
            }
        })

        val bottomNavBarPositionY = binding.bottomNavigation.y

        navController.addOnDestinationChangedListener(object: NavController.OnDestinationChangedListener{
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                if(destination.id == R.id.signIn || destination.id == R.id.signUp){
                    binding.bottomNavigation.animate()
                        .translationY(bottomNavBarPositionY + 500f)
                        .setDuration(250)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                binding.bottomNavigation.visibility = View.GONE
                            }
                        })
                        .start()
                } else {
                    binding.bottomNavigation.animate()
                        .translationY(bottomNavBarPositionY)
                        .setDuration(250)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationStart(animation: Animator) {
                                binding.bottomNavigation.visibility = View.VISIBLE
                            }
                        })
                        .start()
                }
            }

        })

        //check if user signed in (sign out if not)
        if(!viewModel.isUserSignedIn()) navController.navigate(
            R.id.signIn,
            null,
            NavOptions.Builder().setPopUpTo(R.id.home, true).build()
        )

        //check if user can access admin dashboard
        if(!viewModel.canAccessAdminDashboard()) binding.bottomNavigation.menu.findItem(R.id.dashboard).isVisible = false

    }

    fun showAdminDashboard(isAdmin: Boolean){
        binding.bottomNavigation.menu.findItem(R.id.dashboard).isVisible = isAdmin
    }
}