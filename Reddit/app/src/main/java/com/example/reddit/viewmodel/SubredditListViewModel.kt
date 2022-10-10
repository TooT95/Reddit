package com.example.reddit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.reddit.model.subreddit.Subreddit
import com.example.reddit.repository.SubredditRepository
import com.example.reddit.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubredditListViewModel @Inject constructor(
    private val repository: SubredditRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        toastMutableLiveData.postValue("Error on $coroutineContext, message ${throwable.message}")
    }

    private val subredditListMutableLiveData = MutableLiveData<List<Subreddit>>()
    private val subredditListingMutableLiveData = MutableLiveData<List<Subreddit>>()
    private val toastMutableLiveData = SingleLiveEvent<String>()

    val subredditListLiveData: LiveData<List<Subreddit>>
        get() = subredditListMutableLiveData
    val subredditListingLiveData: LiveData<List<Subreddit>>
        get() = subredditListMutableLiveData
    val toastLiveData: LiveData<String>
        get() = toastMutableLiveData

    fun getNewSubredditList() {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            subredditListMutableLiveData.postValue(repository.getSubredditList(SubredditRepository.METHOD_NEW_SUBREDDIT_LIST))
        }
    }

    fun getPopularSubredditList() {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            subredditListMutableLiveData.postValue(repository.getSubredditList(SubredditRepository.METHOD_POPULAR_SUBREDDIT_LIST))
        }
    }

    fun getSubredditListing(subredditName: String) {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            toastMutableLiveData.postValue("Listing size: ${
                repository.getSubredditListingList(subredditName).size.toString()
            }")
        }
    }
}