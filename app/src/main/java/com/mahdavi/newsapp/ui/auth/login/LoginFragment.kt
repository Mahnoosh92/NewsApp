package com.mahdavi.newsapp.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.databinding.FragmentLoginBinding
import com.mahdavi.newsapp.ui.BaseFragment
import com.mahdavi.newsapp.utils.InternalDeepLinkHandler
import com.mahdavi.newsapp.utils.extensions.action
import com.mahdavi.newsapp.utils.extensions.getQueryTextStateFlow
import com.mahdavi.newsapp.utils.extensions.setStrockColor
import com.mahdavi.newsapp.utils.extensions.shortSnackBar
import com.mahdavi.newsapp.utils.widgets.ProgressButtonCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUi() {
        /*NO_OP*/
    }

    override fun setupCollectors() {
        viewModel.loginUiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { loginUiState ->
                binding.apply {
                    loginUiState.areInputsValid?.let {
                        loginButton.isEnabled(loginUiState.areInputsValid)
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
                        loginButton.setLoading(false)
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

            loginButton.onClick(object : ProgressButtonCallback {
                override fun onClick() {
//                    viewModel.triggerNotification(title = R.string.notification_title, message = R.string.notification_message, R.drawable.ic_baseline_notifications_24)
                    loginButton.setLoading(true)
                    viewModel.login(username.text.toString(), password.text.toString())
                }
            })

            signupText.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
        }
    }

    private fun navigateToHome() {
//        activity?.changeNavHostGraph(R.navigation.tabs_graph, R.id.headlineFragment)
        findNavController().popBackStack(R.id.loginFragment, true)
        findNavController().navigate(InternalDeepLinkHandler.TABS.toUri())
    }
}