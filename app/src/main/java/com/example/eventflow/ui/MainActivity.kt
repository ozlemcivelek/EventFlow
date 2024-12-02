package com.example.eventflow.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.eventflow.BaseProgress
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

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.fab.setOnClickListener{
            navController.navigate(R.id.eventDetailFragment)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.eventDetailFragment) {
                binding.bottomAppBar.visibility = View.GONE
                binding.fab.visibility = View.GONE
            } else {
                binding.bottomAppBar.visibility = View.VISIBLE
                binding.fab.visibility = View.VISIBLE
            }
        }

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