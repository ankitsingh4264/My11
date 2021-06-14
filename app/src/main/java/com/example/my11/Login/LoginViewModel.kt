package com.example.my11.Login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my11.beans.User
import com.example.my11.Repository

class LoginViewModel: ViewModel() {
    private  val FirebaseDatabase= Repository()
    var userAdded : MutableLiveData<Boolean> = MutableLiveData()
    var userexits : MutableLiveData<Boolean> = MutableLiveData()

    fun adduser(user: User){
        userAdded = FirebaseDatabase.userUpload(user)
    }

    fun check(email:String)
    {
        userexits = FirebaseDatabase.userExists(email)
    }

}