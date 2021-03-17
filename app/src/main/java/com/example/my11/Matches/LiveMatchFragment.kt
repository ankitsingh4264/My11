package com.example.my11.Matches

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my11.API.RetrofitInstance_NewMatch
import com.example.my11.DataClass.Matche
import com.example.my11.DataClass.NewMatch
import com.example.my11.R
import kotlinx.android.synthetic.main.fragment_future_match.*
import kotlinx.android.synthetic.main.fragment_l_ive_match.*
import retrofit2.Call
import retrofit2.Response


class LiveMatchFragment : Fragment() {

    lateinit var LiveMatch:ArrayList<Matche>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_l_ive_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LiveMatch = ArrayList()

        recycler_live_match.layoutManager= LinearLayoutManager(context)

        getMatch()
    }

    private fun getMatch() {


        val res = RetrofitInstance_NewMatch.cricInstanceforNewMatchApi.matches(1)
        res.enqueue(object : retrofit2.Callback<NewMatch> {
            override fun onResponse(call: Call<NewMatch>, response: Response<NewMatch>) {
                val result = response.body()?.matches
                //Log.i("raj", result.toString())

                for (i in 0..result!!.size - 1) {
                    if (result?.get(i).matchStarted) {
                        LiveMatch.add(result?.get(i))
                    }
                }
                val mAdapter = LiveMatchAdapter(LiveMatch)
                recycler_live_match.adapter = mAdapter
            }

            override fun onFailure(call: Call<NewMatch>, t: Throwable) {
                Log.i("ankit", t.message.toString())
            }

        })
    }

}