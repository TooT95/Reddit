package com.example.reddit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.reddit.model.Account
import com.example.reddit.repository.AccountRepository
import com.example.reddit.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AccountRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        toastMutableLiveData.postValue("Error on $coroutineContext, message ${throwable.message}")
    }

    private val accountMutableLiveData = MutableLiveData<Account>()
    private val accountSubredditCountMutableLiveData = SingleLiveEvent<Int>()
    private val toastMutableLiveData = SingleLiveEvent<String>()
    private val friendListMutableLiveData = MutableLiveData<List<Account>>()
    private val friendInfoMutableLiveData = MutableLiveData<Account>()

    val accountLiveData: LiveData<Account>
        get() = accountMutableLiveData

    val accountSubredditCountLiveData: LiveData<Int>
        get() = accountSubredditCountMutableLiveData

    val toastLiveData: LiveData<String>
        get() = toastMutableLiveData

    val friendListLiveData: LiveData<List<Account>>
        get() = friendListMutableLiveData

    val friendInfoLiveData: LiveData<Account>
        get() = friendInfoMutableLiveData

    fun getFriendList() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            friendListMutableLiveData.postValue(repository.getFriendList())
        }
    }

    fun getFriendInfo(friendName: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            friendInfoMutableLiveData.postValue(repository.getFriendInfo(friendName))
        }
    }

    fun getAccountInfo() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            accountMutableLiveData.postValue(repository.getMe())
            accountSubredditCountMutableLiveData.postValue(repository.getSubredditList())
        }
    }
}