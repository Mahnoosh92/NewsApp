package com.mahdavi.newsapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.mahdavi.newsapp.data.repository.NewsRepositoryImpl
import com.mahdavi.newsapp.databinding.FragmentHomeBinding
import com.mahdavi.newsapp.ui.BaseFragment

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            NewsRepositoryImpl()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUi() {
        viewModel.
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