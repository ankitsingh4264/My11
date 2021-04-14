package com.example.my11.Profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.my11.R
import com.example.my11.Utils.latitude
import com.example.my11.Utils.longitude
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.IOException
import java.util.*


const val STORAGE_REQUEST_CODE = 100

var dpURI: Uri?=null

class ProfileFragment : Fragment() {

    private lateinit var profilemvvm: ProfileViewModel

    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        val geocoder = Geocoder(context, Locale.getDefault())
        var result: String? = null
        try {
            val addressList = geocoder.getFromLocation(latitude!!.toDouble(), longitude!!.toDouble(), 1)
            if (addressList != null && addressList.size > 0) {
                val address = addressList[0]
                val sb = StringBuilder()
                sb.append(address.locality).append(" , ")
                sb.append(address.countryName)
                result = sb.toString()
            }
        } catch (e: IOException) {
            Log.e("Location Address Loader", "Unable connect to Geocoder", e)
        }

        location.text = result

        profilemvvm= ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)

        profilemvvm.getcurruser()
        profilemvvm.curruser.observe(viewLifecycleOwner,
                Observer {
                    txt_id.text = it!!.email
                    edt_name.setText(it!!.name)
                    txtname.text = it!!.name
                    if(it.phone!=null)
                        edt_phn.setText(it.phone)
                    if (it.picture != null && it?.picture != "") {

                        Glide.with(this).load(it?.picture)
                                .into(img_dp)

                    }
                    if (it.picture != null && it?.picture != "") {

                        Glide.with(this).load(it?.picture)
                                .into(cover_dp)

                    }
                })


        save_button.setOnClickListener {
            if(edt_name.text==null && edt_phn.text==null)
            {
                return@setOnClickListener
            }
            else
            {
                val phoneNo = edt_phn.text.toString()
                val name = edt_name.text.toString()
                profilemvvm.updatecurruser(name,phoneNo)
                profilemvvm.updatedcurruser.observe(viewLifecycleOwner,
                        {
                            if(it==true)
                            {
                                txtname.text=name
                            }
                        })
            }

        }

        edit_dp.setOnClickListener {
            if(takepermissions())
            {
                choosePhotofromGallery(STORAGE_REQUEST_CODE)




            }
        }

        logout.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(requireActivity(), OnCompleteListener<Void?> {

                    view?.findNavController()?.navigate(R.id.action_profileFragment_to_loginFragment)
                })
    }

    private  fun takepermissions():Boolean{

        var check=false;
        Dexter.withContext(requireActivity()).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport?) {
                if (multiplePermissionsReport!!.areAllPermissionsGranted()) {
                    check = true;
                } else {
                    Toast.makeText(
                            requireActivity(),
                            "Please provide permission to access this function",
                            Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    permissionToken: PermissionToken?
            ) {
                permissionToken!!.continuePermissionRequest()
            }

        }
        ).onSameThread().check()

        if (check) return true;
        return false;
    }

    private fun choosePhotofromGallery(requestCode: Int){
        val galleryintent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryintent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            //gst 1
            if (requestCode == STORAGE_REQUEST_CODE && data != null) {
                dpURI = data.data

                profilemvvm.uploadDocumentsToFirebase(dpURI!!)
                profilemvvm.mUserPPuploaded.observe(viewLifecycleOwner,
                        {
                            if(it==true)
                            {
                                Log.i("jja", dpURI.toString())
                                Glide.with(this).load(dpURI)
                                        .into(cover_dp)
                                Glide.with(this).load(dpURI)
                                        .into(img_dp)
                            }
                        })
            }
        }
    }


}