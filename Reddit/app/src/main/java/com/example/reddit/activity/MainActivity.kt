package com.example.reddit.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.reddit.R
import com.example.reddit.databinding.ActivityMainBinding
import com.example.reddit.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showMainNavGraph(false)
        startMainFragment()
    }

    fun showMainNavGraph(show: Boolean) {
        binding.fragmentauth.isVisible = !show
        binding.fragment.isVisible = show
        binding.bnavMain.isVisible = show
        if (show) {
            val hostFragment = supportFragmentManager.findFragmentById(R.id.fragment)
            if (hostFragment != null) {
                val navController = hostFragment.findNavController()
                setupWithNavController(binding.bnavMain, navController)
            }
        }
    }


    private fun startMainFragment() {
        if (Utils.onboardPassed(this))
            (supportFragmentManager.findFragmentById(R.id.fragmentauth) as NavHostFragment).navController.apply {
                val graph = navInflater.inflate(R.navigation.nav_graph_oauth)
                graph.setStartDestination(R.id.authFragment)
                setGraph(graph, intent.extras)
            }
    }
}