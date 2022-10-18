package com.example.reddit.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.reddit.repository.NetworkRepository
import com.example.reddit.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val repository: NetworkRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val errorScope = CoroutineExceptionHandler { coroutineContext, throwable ->
        toastMutableLiveData.postValue("Context:$coroutineContext message:${throwable.localizedMessage}")
    }

    private val accessTokenGotMutableLiveData = MutableLiveData<Boolean>()
    private val toastMutableLiveData = SingleLiveEvent<String>()

    val accessTokenGotLiveData: LiveData<Boolean>
        get() = accessTokenGotMutableLiveData

    val toastLiveData: LiveData<String>
        get() = toastMutableLiveData

    fun getAuthToken() {
        viewModelScope.launch(errorScope + Dispatchers.IO) {
            accessTokenGotMutableLiveData.postValue(repository.authTokenGot())
        }
    }

}