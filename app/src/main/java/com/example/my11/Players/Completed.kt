package com.example.my11.Players

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.my11.R
import com.example.my11.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_completed.*


class Completed : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    //close app
                   return

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        return inflater.inflate(R.layout.fragment_completed, container, false)
    }


    lateinit var completedViewModel:CompletedViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().bottomNav.visibility=View.GONE


        completedViewModel= ViewModelProvider(requireActivity()).get(CompletedViewModel::class.java)

        completedViewModel.makePrediction(Utils.prediction!!)

        completedViewModel.placed.observe(requireActivity(),
        Observer {
            if (it){
                pb_animation.visibility=View.GONE
                pb_text.visibility=View.GONE
                sprinkle_anime.visibility=View.VISIBLE;

                complete_anime.visibility=View.VISIBLE
                success.visibility=View.VISIBLE
                rl_finish.visibility=View.VISIBLE
            }
        })

        rl_finish.setOnClickListener {

            view.findNavController().navigate(R.id.action_completed_to_homeFragment)

        }




    }



}