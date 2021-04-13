package com.crazylegend.subhub

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.subhub.core.AbstractActivity
import com.crazylegend.subhub.databinding.ActivityMainBinding
import com.crazylegend.subhub.di.extensions.setupWithNavController
import com.crazylegend.viewbinding.viewBinder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AbstractActivity() {

    override val binding by viewBinder(ActivityMainBinding::inflate)
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState

    }

    private fun setupBottomNavigationBar() {

        val navGraphIds = listOf(
                R.navigation.main,
                R.navigation.settings)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = binding.bottomNav.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.fragmentContainer,
                intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this) {
            setupActionBarWithNavController(it)
            listenToDestinationChanges(it)

        }
        currentNavController = controller

    }

    private fun listenToDestinationChanges(navController: NavController?) {
        navController?.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in hideBottomList) hideBottomNav() else showBottomNav()
        }
    }

    private fun hideBottomNav() {
        binding.bottomNav.gone()
    }

    private fun showBottomNav() {
        binding.bottomNav.visible()
    }

    private val hideBottomList = listOf(
            R.id.loadSubtitlesFragment
    )

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

}
