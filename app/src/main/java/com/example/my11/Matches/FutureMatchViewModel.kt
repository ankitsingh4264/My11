package com.example.my11.Matches

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my11.Repository
import com.example.my11.beans.User

class FutureMatchViewModel : ViewModel(){
    private  val FirebaseDatabase= Repository()
    var idAdded : HashSet<String> = HashSet()
    var curruser : MutableLiveData<User> = MutableLiveData()



    fun getMatchId(email: String){
        idAdded = FirebaseDatabase.getPlacedMactchID(email)
    }
    fun getCurrentUser(){
       curruser= FirebaseDatabase.getuser()
    }

}