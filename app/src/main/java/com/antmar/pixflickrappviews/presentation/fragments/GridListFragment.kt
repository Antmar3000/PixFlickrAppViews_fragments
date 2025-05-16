package com.antmar.pixflickrappviews.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antmar.pixflickrappviews.R
import com.antmar.pixflickrappviews.data.entity.Picture
import com.antmar.pixflickrappviews.databinding.FragmentGridListBinding
import com.antmar.pixflickrappviews.presentation.adapters.GridListAdapter
import com.antmar.pixflickrappviews.presentation.fragments.base.BaseFragment
import com.antmar.pixflickrappviews.presentation.fragments.base.ClickListener
import com.antmar.pixflickrappviews.presentation.viewmodels.FragmentsSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class GridListFragment() : BaseFragment<FragmentGridListBinding>() {

    private val viewModel: FragmentsSharedViewModel by activityViewModels()
    private val navController by lazy { findNavController() }

    private val listener = object : ClickListener {
        override fun openPicture(picture: Picture) {
            viewModel.getHighResUrl(picture.url)

            navController.navigate(R.id.picture_view_fragment)
        }
    }

    private val adapter = GridListAdapter(listener)

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGridListBinding
        get() = FragmentGridListBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        binding.button.setOnClickListener {
            viewModel.clearDB()
        }

    }

    private fun setRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(activity, 3)
            adapter = this@GridListFragment.adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = this@apply.layoutManager as GridLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (totalItemCount <= lastVisibleItemPosition + 5) {
                        viewModel.currentPage++
                        viewModel.getApiListAndInsert(viewModel.currentPage)
                    }
                }
            })
        }

        viewModel.pictureListState.flowWithLifecycle(
            viewLifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).onEach { list ->
            adapter.submitList(list)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

}