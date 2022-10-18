package com.example.reddit.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.reddit.model.subreddit.SubredditListing
import com.example.reddit.repository.SubredditListingRepository
import com.example.reddit.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubredditListingViewModel @Inject constructor(
    private val repository: SubredditListingRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        toastMutableLiveData.postValue("Error on $coroutineContext, message ${throwable.message}")
    }

    private val subredditListingMutableLiveData = MutableLiveData<List<SubredditListing>>()
    private val subredditSavedListingMutableLiveData = SingleLiveEvent<List<SubredditListing>>()
    private val subredditNewListingMutableLiveData = MutableLiveData<List<SubredditListing>>()
    private val listingSaveMutableLiveData = SingleLiveEvent<Int?>()
    private val toastMutableLiveData = SingleLiveEvent<String>()

    val listingSaveLiveData: LiveData<Int?>
        get() = listingSaveMutableLiveData
    val subredditListingLiveData: LiveData<List<SubredditListing>>
        get() = subredditListingMutableLiveData
    val subredditSavedListingLiveData: LiveData<List<SubredditListing>>
        get() = subredditSavedListingMutableLiveData
    val subredditNewListingLiveData: LiveData<List<SubredditListing>>
        get() = subredditNewListingMutableLiveData
    val toastLiveData: LiveData<String>
        get() = toastMutableLiveData

    fun getSubredditListing(
        subredditName: String,
        after: String? = null,
        uploadNewSr: Boolean = false,
    ) {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            if (uploadNewSr) {
                subredditNewListingMutableLiveData.postValue(repository.getSubredditListingList(
                    subredditName, after))
            } else {
                subredditListingMutableLiveData.postValue(repository.getSubredditListingList(
                    subredditName, after))
            }
        }
    }

    fun saveUnsaveListing(listing: SubredditListing, index: Int) {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            listingSaveMutableLiveData.postValue(repository.saveUnsaveListing(listing.fullName,
                listing.saved,
                index))
        }
    }

    fun getSavedListing(accountName: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            subredditSavedListingMutableLiveData.postValue(repository.getSavedListing(accountName))
        }
    }
}