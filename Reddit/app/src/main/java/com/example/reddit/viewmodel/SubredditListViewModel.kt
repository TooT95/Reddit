package com.example.reddit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.reddit.model.subreddit.Subreddit
import com.example.reddit.model.subreddit.SubredditListing
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
    private val subredditNewListMutableLiveData = MutableLiveData<List<Subreddit>>()
    private val subredditSubscribeMutableLiveData = SingleLiveEvent<Int?>()
    private val toastMutableLiveData = SingleLiveEvent<String>()

    val subredditListLiveData: LiveData<List<Subreddit>>
        get() = subredditListMutableLiveData
    val subredditNewListLiveData: LiveData<List<Subreddit>>
        get() = subredditNewListMutableLiveData
    val subredditSubscribeLiveData: LiveData<Int?>
        get() = subredditSubscribeMutableLiveData
    val toastLiveData: LiveData<String>
        get() = toastMutableLiveData

    fun getNewSubredditList(after: String? = null, uploadNewSr: Boolean = false) {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            if (uploadNewSr) {
                subredditNewListMutableLiveData.postValue(repository.getSubredditList(
                    SubredditRepository.METHOD_NEW_SUBREDDIT_LIST,
                    after))
            } else {
                subredditListMutableLiveData.postValue(repository.getSubredditList(
                    SubredditRepository.METHOD_NEW_SUBREDDIT_LIST,
                    after))
            }
        }
    }

    fun getPopularSubredditList(after: String? = null, uploadNewSr: Boolean = false) {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            if (uploadNewSr) {
                subredditNewListMutableLiveData.postValue(repository.getSubredditList(
                    SubredditRepository.METHOD_POPULAR_SUBREDDIT_LIST,
                    after))
            } else {
                subredditListMutableLiveData.postValue(repository.getSubredditList(
                    SubredditRepository.METHOD_POPULAR_SUBREDDIT_LIST,
                    after))
            }
        }
    }

    fun subUnSubSubreddit(index: Int, subreddit: Subreddit) {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            subredditSubscribeMutableLiveData.postValue(repository.subscribeSubreddit(
                index, subreddit))
        }
    }
}