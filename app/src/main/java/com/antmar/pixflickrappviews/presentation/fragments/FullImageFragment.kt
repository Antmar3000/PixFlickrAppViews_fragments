package com.antmar.pixflickrappviews.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.antmar.pixflickrappviews.R
import com.antmar.pixflickrappviews.databinding.FragmentFullImageBinding
import com.antmar.pixflickrappviews.presentation.fragments.base.BaseFragment
import com.antmar.pixflickrappviews.presentation.viewmodels.FragmentsSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FullImageFragment : BaseFragment<FragmentFullImageBinding>() {

    private val viewModel : FragmentsSharedViewModel by activityViewModels()
    private val navController by lazy { findNavController() }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFullImageBinding
        get() = FragmentFullImageBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentPictureUrl.collect {
                    if (it.isNotEmpty()) loadImage(it)
                }
            }
        }

        binding.closeButton.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun loadImage(url : String) {
        binding.pictureView.load(url)
    }

}