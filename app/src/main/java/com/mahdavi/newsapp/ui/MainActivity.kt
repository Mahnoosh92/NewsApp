package com.mahdavi.newsapp.ui


import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //top toolbar
        setSupportActionBar(findViewById(R.id.my_toolbar))

        //bottom navbar
        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(com.mahdavi.newsapp.R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                com.mahdavi.newsapp.R.id.home,
                com.mahdavi.newsapp.R.id.favorite,
                com.mahdavi.newsapp.R.id.setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setupUiListener()
    }

    private fun setupUiListener() {
        setupNav()
    }

    private fun setupNav() {

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.home -> showBottomNav()
                R.id.favorite -> showBottomNav()
                R.id.setting -> showBottomNav()
                else -> hideBottomNav()
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> hideAppbar()
                else -> showAppbar()
            }
        }
    }

    private fun showBottomNav() {
        binding.navView.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.navView.visibility = View.GONE
    }
    private fun showAppbar() {
        binding.appbar.visibility= View.VISIBLE
    }

    private fun hideAppbar() {
        binding.appbar.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

}