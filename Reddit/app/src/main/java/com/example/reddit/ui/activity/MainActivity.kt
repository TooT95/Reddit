package com.example.reddit.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
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
        showMainNavGraph(show = false, isStart = true)
        startMainFragment()
    }

    fun showMainNavGraph(show: Boolean, isStart: Boolean = true) {
        binding.bnavMain.isVisible = show
        val hostFragment = supportFragmentManager.findFragmentById(R.id.fragmentauth)
        if (hostFragment != null) {
            val navController = hostFragment.findNavController()
            if (show) {
                navController.setGraph(R.navigation.nav_graph)
                setupWithNavController(binding.bnavMain, navController)
            } else {
                if (!isStart) {
                    navController.setGraph(R.navigation.nav_graph_oauth)
                    setupWithNavController(binding.bnavMain, navController)
                    startMainFragment()
                }
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