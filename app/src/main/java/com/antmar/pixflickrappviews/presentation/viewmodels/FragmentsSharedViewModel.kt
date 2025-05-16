package com.antmar.pixflickrappviews.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antmar.pixflickrappviews.data.entity.Picture
import com.antmar.screen_gridlist.domain.usecases.ClearDBUseCase
import com.antmar.screen_gridlist.domain.usecases.GetApiListAndInsertUseCase
import com.antmar.screen_gridlist.domain.usecases.GetDBListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentsSharedViewModel @Inject constructor(
    private val getApiAndInsert : GetApiListAndInsertUseCase,
    private val getDBList : GetDBListUseCase,
    private val clearDBUseCase : ClearDBUseCase
) : ViewModel() {

    private val _pictureListState = MutableStateFlow<List<Picture>>(emptyList())
    val pictureListState get() = _pictureListState.asStateFlow()

    private val _currentPictureUrl = MutableStateFlow<String>("")
    val currentPictureUrl get() = _currentPictureUrl.asStateFlow()

    init {
        getApiListAndInsert()
        getDBList()
    }

    override fun onCleared() {
        super.onCleared()
        clearDB()
    }

    private fun getApiListAndInsert() {
        viewModelScope.launch {
            getApiAndInsert()
        }
    }

    private fun getDBList () {
        viewModelScope.launch {
            getDBList.invoke().collect {
                _pictureListState.value = it
            }
        }
    }

    private fun clearDB () {
        viewModelScope.launch {
            clearDBUseCase()
        }
    }

    fun getHighResUrl(url: String) {
         _currentPictureUrl.value = if (url.endsWith("_m.jpg")) {
            url.replace("_m.jpg", "_b.jpg")
        } else url
    }
}