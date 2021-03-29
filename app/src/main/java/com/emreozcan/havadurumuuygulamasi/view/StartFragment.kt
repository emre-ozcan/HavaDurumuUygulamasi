package com.emreozcan.havadurumuuygulamasi.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import com.emreozcan.havadurumuuygulamasi.R
import com.emreozcan.havadurumuuygulamasi.view.StartFragmentDirections
import www.sanju.motiontoast.MotionToast


class StartFragment : Fragment() {
    private lateinit var dialog : AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (isLocationEnabled(requireContext())&&ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            Handler(Looper.getMainLooper()).postDelayed({
                val action = StartFragmentDirections.actionStartFragmentToHomeFragment(1)
                view.findNavController().navigate(action)
            },3000)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun isLocationEnabled(mContext: Context): Boolean {
        val lm = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)
    }

}