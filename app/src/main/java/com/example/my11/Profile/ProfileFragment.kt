package com.example.my11.Profile

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.my11.MyLocationService
import com.example.my11.R
import com.example.my11.Utils
import com.example.my11.Utils.latitude
import com.example.my11.Utils.longitude
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.internal.Util
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

        activity?.progress_circular!!.visibility=View.VISIBLE
        Utils.location="Delhi"
        profilemvvm= ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)

        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        if(isLocationEnabled(requireContext()))
        {
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
            Utils.location= result.toString()


        }
        else
        {
            location.text=Utils.location
        }





        if(Utils.user_email==null || Utils.user_name==null || Utils.user_dp==null)
        {
            profilemvvm.getcurruser()
            profilemvvm.curruser.observe(viewLifecycleOwner,
                Observer {

                    email.text = it!!.email
                    txtname.setText(it!!.name)
                    txtname.text = it!!.name
                    if(it.phone!=null)
                        phone.setText(it.phone)
                    if (it.picture != null && it?.picture != "") {

                        Glide.with(this).load(it?.picture)
                            .into(img_dp)

                    }
                    Utils.trophies= it!!.totalPoints.toString()
                    trophies.text=it!!.totalPoints.toString()
//                    if (it.picture != null && it?.picture != "") {
//
//                        Glide.with(this).load(it?.picture)
//                            .into(cover_dp)
//
//                    }
                    activity?.progress_circular!!.visibility=View.GONE
                })
        }
        else{
            email.text = Utils.user_email
            txtname.text = Utils.user_name
            txtname.text = Utils.user_name
            trophies.text= Utils.trophies
//        Glide.with(this).load(Utils.user_dp)
//            .into(cover_dp)
            Glide.with(this).load(Utils.user_dp)
                .into(img_dp)
            activity?.progress_circular!!.visibility=View.GONE
        }




//        save_button.setOnClickListener {
//            if(edt_phn.text.toString().length!=10)
//            {
//                edt_phn.error = "fill 10 digits"
//            }
//            else if(edt_phn.text.toString().length==10 && edt_name.text.toString().isBlank())
//            {
//                txtname.text=Utils.user_name
//                edt_name.setText(Utils.user_name)
//            }
//            else
//            {
//                Utils.user_number = edt_phn.text.toString()
//                Utils.user_name = edt_name.text.toString()
//                profilemvvm.updatecurruser(Utils.user_name!!, Utils.user_number!!)
//                profilemvvm.updatedcurruser.observe(viewLifecycleOwner,
//                        {
//                            if(it==true)
//                            {
//                                txtname.text=Utils.user_name
//                            }
//                        })
//            }
//
//        }

        edit_dp.setOnClickListener {
            if(takepermissions())
            {
                choosePhotofromGallery(STORAGE_REQUEST_CODE)
            }
        }

        rl_logout.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {

//        mGoogleSignInClient.signOut()
//                .addOnCompleteListener(requireActivity(), OnCompleteListener<Void?> {
//
//                    view?.findNavController()?.navigate(R.id.action_profileFragment_to_loginFragment)
//                })
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
                                Utils.user_dp= dpURI
//                                Glide.with(this).load(dpURI)
//                                    .into(cover_dp)
                                Glide.with(this).load(dpURI)
                                    .into(img_dp)
                            }
                        })
            }
        }
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val mode = Settings.Secure.getInt(
            context.contentResolver, Settings.Secure.LOCATION_MODE,
            Settings.Secure.LOCATION_MODE_OFF
        )
        return mode != Settings.Secure.LOCATION_MODE_OFF
    }



}