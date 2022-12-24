package com.mahdavi.newsapp.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import coil.load
import coil.result
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.data.dataSource.local.pref.SharedPreferenceDataSource
import com.mahdavi.newsapp.data.model.remote.User
import com.mahdavi.newsapp.databinding.FragmentProfileBinding
import com.mahdavi.newsapp.ui.BaseFragment
import com.mahdavi.newsapp.ui.profile.bottom_sheet.ProfileBottomSheetFragment
import com.mahdavi.newsapp.utils.extensions.shortSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by hiltNavGraphViewModels(R.id.setting_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUi() {
        viewModel.loadDataToProfileForm()
    }

    override fun setupCollectors() {
        viewModel.profileUiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { profileUiState ->
                with(profileUiState) {
                    user?.let {
                        binding.apply {
                            userDisplayName.setText(it.displayName)
                            userEmail.setText(it.email)
//                            userPhotoUri.setImageURI(it.photoUrl)
                            userPhotoUri.load(it.photoUrl)
                        }
                    }
                    error?.let {
                        binding.root.shortSnackBar(this.error)
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun setupListeners() {
        binding.apply {
            profileCamera.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToProfileBottomSheetFragment())
            }
            submitButton.setOnClickListener {
                viewModel.updateProfileInformation()
            }
        }
    }
}