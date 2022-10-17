package com.example.reddit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.reddit.model.CommentListing
import com.example.reddit.repository.CommentRepository
import com.example.reddit.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val repository: CommentRepository,
    application: Application,
) :
    AndroidViewModel(application) {

    private val exceptionScope = CoroutineExceptionHandler { coroutineContext, throwable ->
        toastMutableLiveData.postValue("Error on $coroutineContext, message ${throwable.message}")
    }
    private val commentInfoMutableLivedata = SingleLiveEvent<CommentListing>()
    private val toastMutableLiveData = SingleLiveEvent<String>()

    val commentInfoLiveData: LiveData<CommentListing>
        get() = commentInfoMutableLivedata
    val toastLiveData: LiveData<String>
        get() = toastMutableLiveData

    fun getCommentInfo(commentLink: String) {
        viewModelScope.launch(exceptionScope + Dispatchers.IO) {
            commentInfoMutableLivedata.postValue(repository.getCommentInfo(commentLink))
        }
    }
}