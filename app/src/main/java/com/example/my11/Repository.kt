package com.example.my11

import android.annotation.TargetApi
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.contentValuesOf
import androidx.lifecycle.MutableLiveData
import com.example.my11.API.CricService
import com.example.my11.API.RetrofitInstance
import com.example.my11.beans.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.tasks.await
import org.w3c.dom.Document
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
            //Log.i("lakki",it.toObject(User::class.java).toString())
            data.value=it.toObject(User::class.java)
        }
        return data
    }

    fun get_top_user():MutableLiveData<ArrayList<User>>{
        var data:MutableLiveData<ArrayList<User>> = MutableLiveData()
        val list = ArrayList<User>()
        firestoreDB.collection("users").orderBy("totalPoints",Query.Direction.DESCENDING).limit(2).get().addOnSuccessListener {
            for (i in 1 ..it!!.documents.size) {
                //Log.i("lakki",it.documents[0].toObject(User::class.java).toString())
                list.add(it.documents[i-1].toObject(User::class.java)!!)
            }


            data.value = list
        }
        return data
    }

    fun userExists(email: String): MutableLiveData<Boolean> {

        val isUserExist: MutableLiveData<Boolean> = MutableLiveData()

        firestoreDB.collection("users").document(email)
            .get().addOnSuccessListener {

                isUserExist.value = it.toObject(User::class.java) !=null
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

   suspend fun getPredictedMatchesSuspend(context:Context) {
       if(auth.currentUser==null)   return
        val email=auth.currentUser.email
       var result=0;
       val arr :ArrayList<Predicted> = ArrayList();



       val matches:ArrayList<Predicted> = ArrayList()
       CoroutineScope(IO).launch {

           val pmatches =
               firestoreDB.collection("users").document(email).collection("Predicted").get()
                   .await().documents
           for (data in pmatches) {
               val k = data.toObject(Predicted::class.java)
               if (k != null) {
                   matches.add(k)
               }
           }



           for (mat in matches){

               if (!mat.winnerTeam!!.isEmpty()){
                   result++;
                   arr.add(mat)
                   continue
                   //notification already received
               }


               val playres=retrofitCric.getCompletedMatch(mat.matchId)

               playres.enqueue(object : retrofit2.Callback<CompletedMatch>{
                   @TargetApi(Build.VERSION_CODES.N)
                   override fun onResponse(call: Call<CompletedMatch>, response: Response<CompletedMatch>) {
                       val p: CompletedMatch? =response.body()
                       result++;
                       if(p!!.data!!.winner_team==null)
                       {

                       }
                       else if (p.data!!.winner_team!!.isEmpty()){


                       }else {
                           //batting pts
                           var totalPoints = 0;
                           for (item in p.data.batting!!) {
                               for (players in item!!.scores!!) {
                                   val id = players!!.pid!!
                                   val runs = players.R!!.toInt()
                                   if (mat.predictedPlayers.containsKey(id)) {
                                       mat.predictedPlayers[id] = runs
                                       totalPoints += runs
                                   }
                               }

                           }
                           //bowling pts
                           for (item in p.data.bowling!!) {
                               for (players in item!!.scores!!) {
                                   val id = players!!.pid!!
                                   val wkts = players.W!!.toInt() * 50
                                   if (mat.predictedPlayers.containsKey(id)) {
                                       mat.predictedPlayers.put(
                                           id,
                                           mat.predictedPlayers.getOrDefault(id, 0) + wkts
                                       )
                                       totalPoints += wkts
                                   }
                               }

                           }
                           mat.winnerTeam = p.data.winner_team!!
                           //settting total match points
                           mat.points = totalPoints
                           if (mat.predictedTeam==mat.winnerTeam) totalPoints+=100;

                           mat.points=totalPoints

                           Notification(context).createNotification(mat.team1 +" vs "+mat.team2,"Congrats you got $totalPoints points see you on leaderboard")


                           firestoreDB.runBatch {
                               //updting after fetching results
                               firestoreDB.collection("users").document(auth.currentUser.email)
                                   .collection("Predicted").document(mat.matchId).set(mat)

                           }
                       }
                   }
                   override fun onFailure(call: Call<CompletedMatch>, t: Throwable) {

                   }
               })
           }





       }


    }


    fun getCompletedMatches(list:ArrayList<Predicted>): MutableLiveData<ArrayList<Predicted>> {
        val res:MutableLiveData<ArrayList<Predicted>> = MutableLiveData()
        val arr :ArrayList<Predicted> = ArrayList();
        var result=0;
        for (mat in list){

            if (!mat.winnerTeam!!.isEmpty()){
                result++;
                arr.add(mat)

                if (result==list.size) {

                    res.value=arr;
                }
                continue
            }


            val playres=retrofitCric.getCompletedMatch(mat.matchId)

            playres.enqueue(object : retrofit2.Callback<CompletedMatch>{
                @TargetApi(Build.VERSION_CODES.N)
                override fun onResponse(call: Call<CompletedMatch>, response: Response<CompletedMatch>) {
                    val p: CompletedMatch? =response.body()
                    Log.d("ankit", "onResponse: $response")
                    result++;
                    if(p!!.data!!.winner_team==null)
                    {
                        if (result==list.size) {

                            res.value=arr;
                        }
                    }
                    else if (p.data!!.winner_team!!.isEmpty()){
                        if (result==list.size) {

                            res.value=arr;
                        }

                    }else {
                        if (p.data != null)
                            Log.i("ankit", p.data!!.winner_team.toString())
                        //batting pts
                        var totalPoints = 0;
                        for (item in p.data!!.batting!!) {
                            for (players in item!!.scores!!) {
                                val id = players!!.pid!!
                                val runs = players.R!!.toInt()
                                if (mat.predictedPlayers.containsKey(id)) {
                                    mat.predictedPlayers[id] = runs
                                    totalPoints += runs
                                }
                            }

                        }
                        //bowling pts
                        for (item in p.data.bowling!!) {
                            for (players in item!!.scores!!) {
                                val id = players!!.pid!!
                                val wkts = players.W!!.toInt() * 50
                                if (mat.predictedPlayers.containsKey(id)) {
                                    mat.predictedPlayers.put(
                                        id,
                                        mat.predictedPlayers.getOrDefault(id, 0) + wkts
                                    )
                                    totalPoints += wkts
                                }
                            }

                        }
                        mat.winnerTeam = p.data.winner_team!!
                        //settting total match points
                        mat.points = totalPoints
                        if (mat.predictedTeam==mat.winnerTeam) totalPoints+=100;
                        mat.points=totalPoints


                        firestoreDB.runBatch {
                            //updting after fetching results
                            firestoreDB.collection("users").document(auth.currentUser.email)
                                .collection("Predicted").document(mat.matchId).set(mat)

                        }.addOnSuccessListener {

                            arr.add(mat)

                            if (result == list.size) {

                                res.value = arr;
                            }
                        }
                    }








                }
                override fun onFailure(call: Call<CompletedMatch>, t: Throwable) {
                    Log.d("ankit", "onFailure: ${t.message}")
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