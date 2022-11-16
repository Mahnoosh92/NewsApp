package com.mahdavi.newsapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mahdavi.newsapp.databinding.FragmentFavoriteBinding
import com.mahdavi.newsapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


class FavoriteFragment : BaseFragment() {
    
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUi() {

    }

    override fun setupObservers() {

    }

    override fun setupListeners() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}