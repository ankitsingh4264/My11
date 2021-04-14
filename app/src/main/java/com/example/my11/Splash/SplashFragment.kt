package com.example.my11.Splash

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.my11.MainActivity
import com.example.my11.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlin.math.hypot
import kotlin.math.max


class SplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({

            //check if user is already logged in
            if (FirebaseAuth.getInstance().currentUser != null) {
//                val myView: View = awesome_card
//                YoYo.with(Techniques.FadeIn)
//                    .duration(100)
//                    .repeat(1)
//                    .playOn(splash_logo);
//                YoYo.with(Techniques.RollIn)
//                    .duration(10000)
//                    .repeat(1)
//                    .playOn(predict_win_text);
//
//                // get the center for the clipping circle
//
//                // get the center for the clipping circle
//                val cx = (myView.left + myView.right) / 2
//                val cy = (myView.top + myView.bottom) / 2
//
//                // get the final radius for the clipping circle
//
//                // get the final radius for the clipping circle
//                val dx = max(cx, myView.width - cx)
//                val dy = max(cy, myView.height - cy)
//                val finalRadius = hypot(dx.toDouble(), dy.toDouble()).toFloat()
//
//                // Android native animator
//
//                // Android native animator
//                val animator: Animator =
//                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0F, finalRadius)
//                animator.interpolator = AccelerateDecelerateInterpolator()
//                animator.duration = 1000
//                animator.start()
                //animator.end()


                    // You can declare your desire activity here to open after finishing splash screen. Like MainActivity
                context?.startActivity(Intent(context, MainActivity::class.java))

            }
            else{
                view.findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }


        }, 1000)


    }


}