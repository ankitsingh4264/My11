package com.example.my11

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.my11.API.CricService
import com.example.my11.API.RetrofitInstance
import com.example.my11.beans.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
                Log.i("jaasd",response.toString())
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
        val id = auth.currentUser!!.email
        Log.i("luc",id)
        var pos:MutableLiveData<Boolean> = MutableLiveData()

        firestoreDB.collection("users").document(id).set(user).addOnSuccessListener {
            pos.value=true
        }.addOnFailureListener{
            Log.e("fad",it.toString() )
            pos.value=false
        }
        return pos
    }
    fun userUpdate(name : String, phone:String) : MutableLiveData<Boolean> {
        val id = auth.currentUser!!.email
        var pos:MutableLiveData<Boolean> = MutableLiveData()

        firestoreDB.collection("users").document(id)
                .update(mapOf(
                        "name" to name,
                        "phone" to phone
                ))
                .addOnSuccessListener { pos.value=true }
                .addOnFailureListener { pos.value=false }
        return pos
    }


    fun getuser():MutableLiveData<User>{
        val data:MutableLiveData<User> = MutableLiveData()
        val id= auth.currentUser?.email
        if (id==null) return data
        firestoreDB.collection("users").document(id!!).get().addOnSuccessListener {
            data.value=it.toObject(User::class.java)
        }
        return data
    }

    fun userExists(email: String): MutableLiveData<Boolean> {

        val isUserExist: MutableLiveData<Boolean> = MutableLiveData()

        firestoreDB.collection("users").document(email)
                .get().addOnSuccessListener {

                    if(it.toObject(User::class.java) !=null)
                        isUserExist.value = true
                    else
                        isUserExist.value = false
                }.addOnFailureListener {
                    isUserExist.value = false
                }

        return isUserExist
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
    fun getPredictedMatches(): MutableLiveData<ArrayList<Predicted>> {
        val email=auth.currentUser.email

        val matches:MutableLiveData<ArrayList<Predicted>> = MutableLiveData()
        firestoreDB.collection("users").document(email).collection("Predicted").get().addOnSuccessListener {
            val temp:ArrayList<Predicted> = ArrayList()
            for (data in it.documents){


                val k=data.toObject(Predicted::class.java)

//                if (k!!.dateTimeGMT > Timestamp.now().toDate())   //todo
                temp.add(k!!)
            }
            matches.value=temp
        }
        return matches
    }
    fun getCompletedMatches(list:ArrayList<Predicted>): MutableLiveData<ArrayList<Predicted>> {
        val res:MutableLiveData<ArrayList<Predicted>> = MutableLiveData()
        val arr :ArrayList<Predicted> = ArrayList();
        var result=0;
        for (mat in list){
//            if (mat.dateTimeGMT!!.substring(5,7).toInt()>Timestamp.now().toDate().month && mat.dateTimeGMT!!.substring(8,10).toInt()>Timestamp.now().toDate().day ){
//                result++;
//                continue
//
//            }


            val playres=retrofitCric.getCompletedMatch(mat.matchId)

            playres.enqueue(object : retrofit2.Callback<CompletedMatch>{
                override fun onResponse(call: Call<CompletedMatch>, response: Response<CompletedMatch>) {
                    val p: CompletedMatch? =response.body()
                    result++;

                   if (p!!.data!=null)
                    Log.i("ankit",p.data!!.winner_team.toString())
                   //batting pts
                   for (item in p.data!!.batting!!){
                          for (players in item!!.scores!!){
                              val id=players!!.pid!!
                              val runs=players.R!!.toInt()
                              if (mat.predictedPlayers!!.containsKey(id)){
                                  mat.predictedPlayers!![id] = runs
                              }
                          }

                   }
                    //bowling pts
                    for (item in p.data.bowling!!){
                        for (players in item!!.scores!!){
                            val id=players!!.pid!!
                            val wkts=players.W!!.toInt()*50
                            if (mat.predictedPlayers!!.containsKey(id)){
                                mat.predictedPlayers!![id] = wkts
                            }
                        }

                    }
                    mat.winnerTeam=p.data.winner_team!!

                    //update in firebase
                    firestoreDB.collection("users").document(auth.currentUser.email).collection("Predicted").document(mat.matchId).set(mat)



                    arr.add(mat)

                    if (result==list.size) {

                        res.value=arr;
                    }

                }
                override fun onFailure(call: Call<CompletedMatch>, t: Throwable) {

                }
            })
        }
        return  res
    }


    fun getPlacedMactchID(email:String):HashSet<String>
    {
        val list:HashSet<String> = HashSet()

        firestoreDB.collection("users").document(email).collection("Predicted").get().addOnSuccessListener {
            //Log.i("TAG", it.toString())

                    for (document in it) {
                Log.i("TAG", "${document.id}")
                list.add(document.id.toString())
            }
        }.addOnFailureListener{

        }
        return list
    }

    fun uploadPictureToFirebase(imageURI: Uri) :MutableLiveData<Boolean>{
        val email = auth.currentUser!!.email
        val storageReference =
            FirebaseStorage.getInstance().getReference("/ProfilePictures/$email")
        val uploadedMutableLiveData:MutableLiveData<Boolean> = MutableLiveData()

        storageReference.putFile(imageURI)
            .addOnSuccessListener {
                storageReference.downloadUrl
                    .addOnSuccessListener {
                        firestoreDB.collection("users").document(email)
                            .update("picture", it.toString())
                    }
            }
            .addOnProgressListener {
            }
            .addOnCompleteListener {
                uploadedMutableLiveData.value=true

            }
        return uploadedMutableLiveData

    }


}
