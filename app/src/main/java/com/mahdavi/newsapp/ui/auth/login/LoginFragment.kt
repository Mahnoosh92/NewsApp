package com.mahdavi.newsapp.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.databinding.FragmentLoginBinding
import com.mahdavi.newsapp.ui.BaseFragment
import com.mahdavi.newsapp.ui.MainActivity
import com.mahdavi.newsapp.utils.InternalDeepLinkHandler
import com.mahdavi.newsapp.utils.extensions.changeNavHostGraph
import com.mahdavi.newsapp.utils.extensions.getQueryTextStateFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUi() {

    }

    override fun setupCollectors() {

    }

    override fun setupListeners() {
        with(binding) {
            viewModel.validateLoginInputs(username.getQueryTextStateFlow(), password.getQueryTextStateFlow())
            loginButton.setOnClickListener {
                navigateToHome()
            }
            signupText.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
        }
    }

    private fun navigateToHome() {
        activity?.changeNavHostGraph(R.navigation.tabs_graph, R.id.homeFragment)
        findNavController().navigate(InternalDeepLinkHandler.TABS.toUri())
    }
}