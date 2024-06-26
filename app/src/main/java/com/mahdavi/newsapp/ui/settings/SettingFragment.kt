package com.mahdavi.newsapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mahdavi.newsapp.databinding.FragmentSettingsBinding
import com.mahdavi.newsapp.ui.BaseFragment
import com.mahdavi.newsapp.utils.extensions.action
import com.mahdavi.newsapp.utils.extensions.shortSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun setupUi() {
        val data = listOf("A","B", "C","D","E", "F", "G","A","B", "C","D","E", "F", "G","A","B", "C","D","E", "F", "G","A","B", "C","D","E", "F", "G","A","B", "C","D","E", "F", "G")
        binding.recycleView.adapter = SettingDataAdapter(data)
    }

    override fun setupCollectors() {

    }

    override fun setupListeners() {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}