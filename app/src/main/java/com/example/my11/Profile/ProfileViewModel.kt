package com.example.my11.Profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my11.DataClass.User
import com.example.my11.Repository

class ProfileViewModel: ViewModel() {
    private  val FirebaseDatabase= Repository()
    var curruser : MutableLiveData<User> = MutableLiveData()
    var updatedcurruser : MutableLiveData<Boolean> = MutableLiveData()

    fun getcurruser(){
        curruser=FirebaseDatabase.getuser()
    }

    fun updatecurruser(n:String,p:String)
    {
        updatedcurruser=FirebaseDatabase.userUpdate(n,p)
    }

    fun updatedpuser(dp:Uri)
    {
        updatedcurruser=FirebaseDatabase.userdpUpdate(dp)
    }
}