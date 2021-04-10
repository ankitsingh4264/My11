package com.example.my11

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.my11.API.CricService
import com.example.my11.API.RetrofitInstance
import com.example.my11.DataClass.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Response

class Repository {
    private val retrofitCric:CricService = RetrofitInstance.cricInstance

    private val firestoreDB = FirebaseFirestore.getInstance()
    private val auth= FirebaseAuth.getInstance()
    lateinit var currUser:User

    fun getSquad(squadId:String) : MutableLiveData<ArrayList<Team>>{

        val data : MutableLiveData<ArrayList<Team>> =MutableLiveData()
        val res= retrofitCric.getSquad(squadId)
        res.enqueue(object  : retrofit2.Callback<Squad>{
            override fun onResponse(call: Call<Squad>, response: Response<Squad>) {
//                 val map:HashMap<String,ArrayList<Players>> = HashMap();
                val result=response.body();
                val temp:ArrayList<Team> = ArrayList();
                for (team in result!!.squad){
                    temp.add(team);
                }
                data.value=temp


            }

            override fun onFailure(call: Call<Squad>, t: Throwable) {
                Log.i("ankit",t.message!!)
            }

        })
        return data
    }
    fun getPlayerDetails(players: ArrayList<Players>) : MutableLiveData<ArrayList<Players>>{
          val res:MutableLiveData<ArrayList<Players>> = MutableLiveData()
        val arr :ArrayList<Players> = ArrayList();
        for (player in players){

            val playres=retrofitCric.getPlayerDetails(player.pid)

            playres.enqueue(object : retrofit2.Callback<Players>{
                override fun onResponse(call: Call<Players>, response: Response<Players>) {
                                val p: Players? =response.body()



                                  arr.add(p!!)
                                    Log.i("ankit",""+ arr.size+" "+players.size)

//                                arr.add(Players(player.pid,player.name,
//                                    response.body()?.playingRole,response.body().bowling,response.bo
//                                ))
                                if (arr.size==players.size) {

                                    res.value=arr;
                                }

                }
                override fun onFailure(call: Call<Players>, t: Throwable) {
                    Log.i("ankit",t.message.toString())

                    }
            })
        }
        return  res


        }


    fun userUpload(user: User) : MutableLiveData<Boolean> {
        val id = auth.currentUser!!.uid
        var pos:MutableLiveData<Boolean> = MutableLiveData()

        firestoreDB.collection("users").document(id).set(user).addOnSuccessListener {
            pos.value=true
        }.addOnFailureListener{
            pos.value=false
        }
        return pos
    }

    fun placePrediction(curr:Predicted): MutableLiveData<Boolean> {
        val id = curr.matchId
        val email=auth.currentUser.email
        var pos:MutableLiveData<Boolean> = MutableLiveData()

        firestoreDB.collection("users").document(email).collection("Predicted").document(id).set(curr).addOnSuccessListener {
            pos.value=true
        }.addOnFailureListener{
            pos.value=false
        }
        return pos

    }

}
