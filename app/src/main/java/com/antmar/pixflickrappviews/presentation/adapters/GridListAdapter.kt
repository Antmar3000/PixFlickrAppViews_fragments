package com.antmar.pixflickrappviews.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.antmar.pixflickrappviews.data.entity.Picture
import com.antmar.pixflickrappviews.databinding.FragmentGridListBinding
import com.antmar.pixflickrappviews.databinding.PictureListItemBinding
import com.antmar.pixflickrappviews.presentation.fragments.base.ClickListener

class GridListAdapter (private val listener: ClickListener) : ListAdapter<Picture, GridListViewHolder> (DiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridListViewHolder {
        val binding = PictureListItemBinding.inflate(LayoutInflater.from(parent.context) , parent, false)
        return GridListViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: GridListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DiffUtil : DiffUtil.ItemCallback<Picture>(){
    override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean {
        return oldItem == newItem
    }


}
