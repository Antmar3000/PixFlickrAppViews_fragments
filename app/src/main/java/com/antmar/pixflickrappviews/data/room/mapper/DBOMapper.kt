package com.antmar.screen_gridlist.data.room.mapper

import com.antmar.screen_gridlist.data.room.dbo.PictureDbo
import com.antmar.pixflickrappviews.data.entity.Picture
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


fun PictureDbo.toEntity(): Picture {
    return Picture(url = this.url, title = this.title)
}

fun Flow<List<PictureDbo>>.toPictureFlow(): Flow<List<Picture>> {
    return this.map { list ->
        list.map { dbo -> dbo.toEntity() }
    }
}