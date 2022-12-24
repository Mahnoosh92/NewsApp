package com.mahdavi.newsapp.ui.profile.bottom_sheet

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mahdavi.newsapp.BuildConfig
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.databinding.FragmentProfileBottomSheetBinding
import com.mahdavi.newsapp.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File

@AndroidEntryPoint
class ProfileBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentProfileBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var latestTmpUri: Uri? = null
    private val viewModel: ProfileViewModel by hiltNavGraphViewModels(R.id.setting_graph)

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    viewModel.setImageUri(uri)
                    findNavController().navigateUp()
                }
            }
        }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                viewModel.setImageUri(uri)
                findNavController().navigateUp()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png").apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            requireContext(), "${BuildConfig.APPLICATION_ID}.provider", tmpFile
        )
    }

    private fun takeImage() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            takeImageButton.setOnClickListener { takeImage() }
            selectImageButton.setOnClickListener { selectImageFromGallery() }
        }
        viewModel.profileUiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { profileUiState ->
                profileUiState.isClosed?.let {
                    if (it) {
                        this.dismiss()
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}