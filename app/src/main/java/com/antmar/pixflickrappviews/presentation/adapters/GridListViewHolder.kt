package com.antmar.pixflickrappviews.presentation.adapters

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.antmar.pixflickrappviews.data.entity.Picture
import com.antmar.pixflickrappviews.databinding.PictureListItemBinding
import com.antmar.pixflickrappviews.presentation.fragments.base.ClickListener

class GridListViewHolder (
    private val binding : PictureListItemBinding,
    private val listener: ClickListener
) : RecyclerView.ViewHolder (binding.root) {

    fun bind(picture: Picture) = with(binding) {
        pictureImage.load(Uri.parse(picture.url)) {
            crossfade(true)
            placeholder(android.R.drawable.ic_menu_search)
        }
        picsItemCardView.setOnClickListener { listener.openPicture(picture) }
    }
}