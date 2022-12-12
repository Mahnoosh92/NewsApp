package com.mahdavi.newsapp.utils.extensions

import android.app.Activity
import androidx.annotation.NavigationRes
import androidx.navigation.fragment.NavHostFragment
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.ui.MainActivity

fun Activity.changeNavHostGraph(@NavigationRes idNavGraph: Int, destination: Int) {
    val navHostFragment: NavHostFragment =
        (this as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
    val navController = navHostFragment.navController
    val navGraph = navController.navInflater.inflate(idNavGraph)
    navGraph.setStartDestination(destination)
    navController.graph = navGraph
}
