package com.mahdavi.newsapp.ui.favorite.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mahdavi.newsapp.ui.favorite.headline.FavouriteHeadlineFragment

private const val NUM_PAGES = 2

class FavouritePageAdapter(private val fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = NUM_PAGES

    override fun createFragment(position: Int): Fragment =
        FavouriteHeadlineFragment.getInstance(position)
}
