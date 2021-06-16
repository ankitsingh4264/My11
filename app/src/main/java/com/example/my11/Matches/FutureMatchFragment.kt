package com.example.my11.Matches

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my11.API.RetrofitInstance
import com.example.my11.beans.Matche
import com.example.my11.beans.NewMatch
import com.example.my11.R
import com.example.my11.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_future_match.*
import retrofit2.Call
import retrofit2.Response


class FutureMatchFragment : Fragment(),FutureMatchAdapter.onitemClick{

    lateinit var FutureMatch:ArrayList<Matche>
    private lateinit var futuremvvm: FutureMatchViewModel
    private lateinit var auth: FirebaseAuth
    var email:String=""
    var played_matchList:HashSet<String> = HashSet()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_future_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().bottomNav.visibility=View.VISIBLE

        auth = Firebase.auth

        email=auth.currentUser.email.toString()

        futuremvvm= ViewModelProvider(requireActivity()).get(FutureMatchViewModel::class.java)

        futuremvvm.getMatchId(email)
        played_matchList=futuremvvm.idAdded
        Utils.SetofplayedMatches=played_matchList
        FutureMatch = ArrayList()

        recycler_future_match.layoutManager= LinearLayoutManager(context)

        getMatch()

    }

    private fun getMatch() {


        val res = RetrofitInstance.cricInstance.matches(1)
        res.enqueue(object : retrofit2.Callback<NewMatch> {
            override fun onResponse(call: Call<NewMatch>, response: Response<NewMatch>) {
                val result = response.body()?.matches
                //Log.i("raj", result.toString())
                Log.i("dad","i")


                for (i in result!!.indices) {
                    //Log.i("lala", email)

                    if (!result.get(i).matchStarted && result?.get(i).squad)
                    {
                        if(played_matchList.contains(result?.get(i).unique_id))
                        {

                        }
                         FutureMatch.add(result.get(i))
                    }



                }
                val mAdapter = FutureMatchAdapter(FutureMatch,this@FutureMatchFragment)
                recycler_future_match.adapter = mAdapter
            }

            override fun onFailure(call: Call<NewMatch>, t: Throwable) {
                Log.i("ankit", t.message.toString())
            }

        })
    }

    override fun onItemClicked(position: Int) {
        Utils.FutureMatchtoPlay=FutureMatch.get(position)
        if(played_matchList.contains(Utils.FutureMatchtoPlay!!.unique_id))
                return

         view?.findNavController()?.navigate(R.id.action_homeFragment_to_play)
    }

}