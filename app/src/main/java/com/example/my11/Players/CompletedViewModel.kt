package com.example.my11.Players

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my11.beans.Predicted
import com.example.my11.Repository

class CompletedViewModel : ViewModel() {
    private  val repo: Repository = Repository();

    var placed:MutableLiveData<Boolean>  = MutableLiveData()

    fun makePrediction(predicted: Predicted){
        placed=repo.placePrediction(predicted)
    }


}