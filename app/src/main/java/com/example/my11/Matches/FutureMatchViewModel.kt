package com.example.my11.Matches

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my11.DataClass.User
import com.example.my11.Repository

class FutureMatchViewModel : ViewModel(){
    private  val FirebaseDatabase= Repository()
    var idAdded : HashSet<String> = HashSet()

    fun getMatchId(email: String){
        idAdded = FirebaseDatabase.getPlacedMactchID(email)
    }
}