package com.antmar.pixflickrappviews.presentation.fragments.base

import com.antmar.pixflickrappviews.data.entity.Picture

interface ClickListener {

    fun openPicture (picture: Picture)
}