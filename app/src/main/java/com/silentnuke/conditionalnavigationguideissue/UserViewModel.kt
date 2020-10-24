package com.silentnuke.conditionalnavigationguideissue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {

    val user = MutableLiveData<User?>(null)

    fun login(username: String, password: String): LiveData<Boolean> {
        user.value = User(username)
        return MutableLiveData(true)
    }
}

data class User(val name: String)
