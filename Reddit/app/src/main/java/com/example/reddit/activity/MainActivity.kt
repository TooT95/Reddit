package com.example.reddit.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.reddit.R
import com.example.reddit.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startMainFragment()
    }

    private fun startMainFragment() {
        if (Utils.onboardPassed(this))
            (supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment).navController.apply {
                val graph = navInflater.inflate(R.navigation.nav_graph)
                graph.setStartDestination(R.id.authFragment)
                setGraph(graph, intent.extras)
            }
    }
}