package com.example.reddit.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.reddit.model.subreddit.Subreddit
import com.example.reddit.repository.SubredditRepository
import com.example.reddit.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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
    private val subredditSearchListMutableLiveData = MutableLiveData<List<Subreddit>?>()
    private val subredditNewListMutableLiveData = MutableLiveData<List<Subreddit>>()
    private val subredditSubscribeMutableLiveData = SingleLiveEvent<Int?>()
    private val toastMutableLiveData = SingleLiveEvent<String>()

    val subredditListLiveData: LiveData<List<Subreddit>>
        get() = subredditListMutableLiveData
    val subredditSearchListLiveData: LiveData<List<Subreddit>?>
        get() = subredditSearchListMutableLiveData
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

    fun getSubscribedSubredditList(after: String? = null, uploadNewSr: Boolean = false) {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            if (uploadNewSr) {
                subredditNewListMutableLiveData.postValue(repository.getSubredditList(
                    SubredditRepository.METHOD_SUBSCRIBED_SUBREDDIT_LIST,
                    after))
            } else {
                subredditListMutableLiveData.postValue(repository.getSubredditList(
                    SubredditRepository.METHOD_SUBSCRIBED_SUBREDDIT_LIST,
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

    fun searchSubreddit(
        changeFlow: Flow<String?>,
    ) {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            changeFlow
                .debounce(500)
                .distinctUntilChanged()
                .mapLatest {
                    if (it == null) {
                        null
                    } else {
                        if (it.isNotEmpty()) {
                            repository.searchSubreddit(it)
                        } else {
                            emptyList()
                        }
                    }
                }
                .collect {
                    subredditSearchListMutableLiveData.postValue(it)
                }
        }
    }
}