package com.mahdavi.newsapp.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.ui.BaseFragment


class SearchFragment : BaseFragment() {
    override fun setupUi() {

    }

    override fun setupCollectors() {

    }

    override fun setupListeners() {

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


}