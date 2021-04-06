package com.example.my11.Matches

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my11.API.RetrofitInstance_NewMatch
import com.example.my11.DataClass.Matche
import com.example.my11.DataClass.NewMatch
import com.example.my11.R
import com.example.my11.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_future_match.*
import retrofit2.Call
import retrofit2.Response


class FutureMatchFragment : Fragment(),FutureMatchAdapter.onitemClick{

    lateinit var FutureMatch:ArrayList<Matche>


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

        FutureMatch = ArrayList()

        recycler_future_match.layoutManager= LinearLayoutManager(context)

        getMatch()

    }

    private fun getMatch() {


        val res = RetrofitInstance_NewMatch.cricInstanceforNewMatchApi.matches(1)
        res.enqueue(object : retrofit2.Callback<NewMatch> {
            override fun onResponse(call: Call<NewMatch>, response: Response<NewMatch>) {
                val result = response.body()?.matches
                //Log.i("raj", result.toString())

                for (i in result!!.indices) {
                    if (!result.get(i).matchStarted && result?.get(i).squad) {
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
         view?.findNavController()?.navigate(R.id.action_homeFragment_to_play)
    }

}