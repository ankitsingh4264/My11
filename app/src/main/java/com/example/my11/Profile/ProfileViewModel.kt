package com.example.my11.Profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my11.beans.User
import com.example.my11.Repository

class ProfileViewModel: ViewModel() {
    private  val FirebaseDatabase= Repository()
    var curruser : MutableLiveData<User> = MutableLiveData()
    var updatedcurruser : MutableLiveData<Boolean> = MutableLiveData()
    var mUserPPuploaded: MutableLiveData<Boolean> = MutableLiveData<Boolean>()


    fun getcurruser(){
        curruser=FirebaseDatabase.getuser()
    }

    fun updatecurruser(n:String,p:String)
    {
        updatedcurruser=FirebaseDatabase.userUpdate(n,p)
    }


    fun uploadDocumentsToFirebase(imageUri: Uri) {
        mUserPPuploaded=FirebaseDatabase.uploadPictureToFirebase(imageUri)
    }
}