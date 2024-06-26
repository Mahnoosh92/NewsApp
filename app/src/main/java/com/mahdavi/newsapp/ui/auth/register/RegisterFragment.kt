package com.mahdavi.newsapp.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.databinding.FragmentLoginBinding
import com.mahdavi.newsapp.databinding.FragmentRegisterBinding
import com.mahdavi.newsapp.ui.BaseFragment
import com.mahdavi.newsapp.ui.auth.login.LoginViewModel
import com.mahdavi.newsapp.utils.extensions.action
import com.mahdavi.newsapp.utils.extensions.getQueryTextStateFlow
import com.mahdavi.newsapp.utils.extensions.setStrockColor
import com.mahdavi.newsapp.utils.extensions.shortSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RegisterFragment : BaseFragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUi() {

    }

    override fun setupCollectors() {
        viewModel.registrationUiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { registrationUiState ->
                binding.apply {
                    registerButton.isEnabled = registrationUiState.areInputsValid
                    registrationUiState.emailInvalidationResult?.let {
                        if (it.isSuccess) {
                            usernameRegister.error = null
                            usernameRegister.setStrockColor(R.color.green_100)
                        } else {
                            usernameRegister.error = getString(it.errorMessage)
                        }
                        viewModel.consumeEmailInvalidationResult()
                    }
                    registrationUiState.passwordInvalidationResult?.let {
                        if (it.isSuccess) {
                            passwordRegister.error = null
                            passwordRegister.setStrockColor(R.color.green_100)
                        } else {
                            passwordRegister.error = getString(it.errorMessage)
                        }
                        viewModel.consumePasswordInvalidationResult()
                    }
                    registrationUiState.registerResult?.let { registerResult ->
                        if (registerResult.isRegistered == true) {
                            binding.root.shortSnackBar(getString(R.string.successful_registration))
                            findNavController().navigateUp()
                        } else {
                            binding.root.shortSnackBar(
                                registerResult.errorMessage ?: getString(R.string.error_general)
                            )
                        }
                        viewModel.consumeRegisterResult()
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun setupListeners() {
        binding.apply {
            with(viewModel) {
                validateRegisterInputs(
                    username.getQueryTextStateFlow(),
                    password.getQueryTextStateFlow()
                )
            }
            registerButton.setOnClickListener {
                viewModel.registerUser(username.text.toString(), password.text.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}