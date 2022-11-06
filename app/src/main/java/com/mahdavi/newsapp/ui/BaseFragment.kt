package com.mahdavi.newsapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    abstract fun setupUi()
    abstract fun setupObservers()
    abstract fun setupListeners()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupObservers()
        setupListeners()
    }
}