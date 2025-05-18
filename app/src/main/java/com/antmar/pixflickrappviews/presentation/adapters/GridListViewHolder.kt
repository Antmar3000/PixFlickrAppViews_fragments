package com.antmar.pixflickrappviews.presentation.adapters

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.antmar.pixflickrappviews.data.entity.Picture
import com.antmar.pixflickrappviews.databinding.PictureListItemBinding
import com.antmar.pixflickrappviews.presentation.fragments.base.ClickListener
import com.google.android.material.progressindicator.CircularProgressIndicator

class GridListViewHolder (
    private val binding : PictureListItemBinding,
    private val listener: ClickListener
) : RecyclerView.ViewHolder (binding.root) {

    fun bind(picture: Picture) = with(binding) {
        pictureImage.load(Uri.parse(picture.url)) {
            crossfade(true)
            listener(
                onStart = { binding.progressBar.visibility = View.VISIBLE},
                onSuccess = { _, _ -> binding.progressBar.visibility = View.INVISIBLE },
                onError = { _, _ -> binding.progressBar.visibility = View.INVISIBLE }
            )
        }
        picsItemCardView.setOnClickListener { listener.openPicture(picture) }
    }
}