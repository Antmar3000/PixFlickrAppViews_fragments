package com.antmar.screen_gridlist.domain.usecases

import com.antmar.screen_gridlist.data.flickr.FlickrRepository
import com.antmar.screen_gridlist.data.room.RoomRepository
import javax.inject.Inject

class GetApiListAndInsertUseCase @Inject constructor(
    private val flickrRepository: FlickrRepository,
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(page : Int) {
        val result = flickrRepository.search(page)
        if (result.isSuccess) {
            roomRepository.insertAll(result.getOrNull() ?: emptyList())
        }
    }
}