package com.example.my11.Leaderboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my11.Repository
import com.example.my11.beans.User

class LeaderboardViewModel: ViewModel() {

    private  val FirebaseDatabase= Repository()
    var toppers : MutableLiveData<ArrayList<User>> = MutableLiveData()


    fun gettopuser(){
        toppers=FirebaseDatabase.get_top_user()
    }

}