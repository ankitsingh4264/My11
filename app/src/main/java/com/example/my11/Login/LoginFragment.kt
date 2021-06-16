package com.example.my11.Login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.my11.beans.User
import com.example.my11.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.logging.Level.INFO


class LoginFragment : Fragment() {

    private val RC_SIGN_IN: Int = 0
    private lateinit var auth: FirebaseAuth
    private lateinit var loginmvvm: LoginViewModel
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    var use:User=User()
    var name:String =""
    var email:String=""
    var dp:String=""

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //appb.visibility=View.INVISIBLE

        auth = Firebase.auth
        loginmvvm= ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        login_google.setOnClickListener {
            signIn()


        }



    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        if(account!=null)
            view?.findNavController()?.navigate(R.id.action_loginFragment_to_homeFragment)
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);




        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    name=user.displayName
                    email=user.email
                    dp=user.photoUrl.toString()
                    //phoneNumber=user.phoneNumber
                     Log.i("raje",email!!)

                    loginmvvm.check(email)
                    loginmvvm.userexits.observe(viewLifecycleOwner,
                            { b ->
                                if (b == true) {
                                    view?.findNavController()?.navigate(R.id.action_loginFragment_to_homeFragment)
                                } else {
                                    Log.i("rajeev",name+email+dp)
                                    use = User(name = name, email = email, picture = dp)
                                    loginmvvm.adduser(use)
                                    loginmvvm.userAdded.observe(viewLifecycleOwner,
                                            {
                                                Log.i("dasd",it.toString())
                                                if (it == true)
                                                    view?.findNavController()?.navigate(R.id.action_loginFragment_to_homeFragment)
                                                else
                                                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                                            })

                                }
                            })


                    //updateUI(user)
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Network Error",
                        Toast.LENGTH_SHORT
                ).show()
                }
            }
    }
}