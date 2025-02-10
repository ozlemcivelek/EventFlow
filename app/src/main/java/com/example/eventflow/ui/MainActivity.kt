package com.example.eventflow.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.eventflow.common.BaseProgress
import com.example.eventflow.R
import com.example.eventflow.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        createBottomNavigationBar()
    }

    private fun createBottomNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
        val rootFragmentIds = setOf(
            R.id.homeFragment,
            R.id.serviceFragment,
            R.id.reservationFragment,
        )
        val configuration = AppBarConfiguration(
            rootFragmentIds,
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )

        NavigationUI.setupWithNavController(binding.bottomAppBar, navController, configuration)

        binding.fab.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.homeFragment -> navController.navigate(R.id.addEventFragment)
                R.id.serviceFragment -> navController.navigate(R.id.serviceDetailFragment)
                R.id.reservationFragment -> navController.navigate(R.id.addEventFragment)
                R.id.accountFragment -> Toast.makeText(this, "Hesap Detayı", Toast.LENGTH_SHORT)
                    .show()

                else -> Toast.makeText(this, "Diğer sayfalardan birindesin", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (!rootFragmentIds.contains(destination.id)) {
                binding.bottomAppBar.visibility = View.GONE
                binding.fab.visibility = View.GONE
            } else {
                binding.bottomAppBar.visibility = View.VISIBLE
                binding.fab.visibility = View.VISIBLE
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val nc =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        return nc.navigateUp() || super.onSupportNavigateUp()
    }

    fun showProgress() {
        BaseProgress().apply {
            isCancelable = true
            show(supportFragmentManager, "progress")
        }
    }

    fun hideProgress() {
        supportFragmentManager.fragments.filterIsInstance<BaseProgress>().forEach {
            it.dismiss()
        }
    }
}