package com.mahdavi.newsapp.ui.onboarding.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mahdavi.newsapp.ui.onboarding.OnBoardingFragment
import com.mahdavi.newsapp.ui.onboarding.child.OnBoardingChildFragment

class OnBoardingFragmentStateAdapter(fragment: Fragment, private val itemsCount: Int) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = itemsCount

    override fun createFragment(position: Int) = OnBoardingChildFragment.getInstance(position)
}