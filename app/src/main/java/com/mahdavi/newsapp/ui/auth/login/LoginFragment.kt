package com.mahdavi.newsapp.ui.auth.login

import android.R
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mahdavi.newsapp.databinding.FragmentLoginBinding
import com.mahdavi.newsapp.utils.InternalDeepLinkHandler
import com.mahdavi.newsapp.utils.extensions.getQueryTextStateFlow
import com.mahdavi.newsapp.utils.extensions.shortSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            username.getQueryTextStateFlow().combine(password.getQueryTextStateFlow()) { username, password ->
                username to password
            }
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .onEach {
                    loginButton.isEnabled = it.first.isNotEmpty() && it.second.isNotEmpty()
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                    viewModel.loginState.collectLatest { loginUiState: LoginUiState ->
                        loginUiState.isLoggedIn?.let {
                            if (loginUiState.isLoggedIn) {
                                val deepLink = InternalDeepLinkHandler.DASHBOARD.toUri()
                                findNavController().popBackStack(
                                    com.mahdavi.newsapp.R.id.loginFragment,
                                    true
                                )
                                findNavController().navigate(deepLink)
                            } else {
                                binding.root.shortSnackBar(loginUiState.error ?: "wrong args") {

                                }
                            }
                        }
                    }
                }
            }

            loginButton.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
//                    flow
//                        .flowOn(Dispatchers.Default)
//                        .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
//                        .collect {
//                            viewModel.login(it.first, it.second)
//                        }
                    findNavController().navigate(InternalDeepLinkHandler.DASHBOARD.toUri())
                }


//
            }
            signupText.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
        }
    }

}