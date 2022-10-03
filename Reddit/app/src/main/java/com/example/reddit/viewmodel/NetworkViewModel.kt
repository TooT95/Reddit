package com.example.reddit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.reddit.model.AuthResponse
import com.example.reddit.repository.NetwokrRepository
import com.example.reddit.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val repository: NetwokrRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val errorScope = CoroutineExceptionHandler { coroutineContext, throwable ->
        toastMutableLiveData.postValue("Context:$coroutineContext message:${throwable.message}")
    }

    private val authResponseMutableLiveData = MutableLiveData<AuthResponse>()
    private val toastMutableLiveData = SingleLiveEvent<String>()

    val authResponseLiveData: LiveData<AuthResponse>
        get() = authResponseMutableLiveData

    val toastLiveData: LiveData<String>
        get() = toastMutableLiveData

    fun getAuthToken() {
        viewModelScope.launch(errorScope + Dispatchers.IO) {
            repository.getAuthToken()
        }
    }

}