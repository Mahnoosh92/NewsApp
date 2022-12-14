package com.mahdavi.newsapp.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.databinding.FragmentLoginBinding
import com.mahdavi.newsapp.ui.BaseFragment
import com.mahdavi.newsapp.ui.MainActivity
import com.mahdavi.newsapp.utils.InternalDeepLinkHandler
import com.mahdavi.newsapp.utils.extensions.*
import com.mahdavi.newsapp.utils.extensions.action
import com.mahdavi.newsapp.utils.extensions.shortSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
        viewModel.loginUiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { loginUiState ->
                binding.apply {
                    loginUiState.areInputsValid?.let {
                        loginButton.isEnabled = loginUiState.areInputsValid
                        viewModel.consumeAreInputsValid()
                    }
                    loginUiState.loginResult?.let { loginResult ->
                        if (loginResult.isLoggedIn == true) {
                            navigateToHome()
                        } else {
                            root.shortSnackBar(
                                loginResult.errorMessage ?: getString(R.string.error_general)
                            ) {
                                action("ok") {
                                    this.dismiss()
                                }
                            }
                        }
                        viewModel.consumeLoginResult()
                    }
                    loginUiState.emailInvalidationResult?.let {
                        with(binding) {
                            if (it.isSuccess) {
                                usernameLogin.error = null
                                usernameLogin.setStrockColor(R.color.green_100)
                            } else {
                                usernameLogin.error = getString(it.errorMessage)
                            }
                        }
                        viewModel.consumeEmailInvalidationResult()
                    }
                    loginUiState.passwordInvalidationResult?.let {
                        with(binding) {
                            if (it.isSuccess) {
                                passwordLogin.error = null
                                passwordLogin.setStrockColor(R.color.green_100)
                            } else {
                                passwordLogin.error = getString(it.errorMessage)
                            }
                        }
                        viewModel.consumePasswordInvalidationResult()
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun setupListeners() {
        with(binding) {
            viewModel.validateInputs(
                username.getQueryTextStateFlow(), password.getQueryTextStateFlow()
            )
            loginButton.setOnClickListener {
                viewModel.login(username.text.toString(), password.text.toString())
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