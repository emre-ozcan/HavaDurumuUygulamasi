package com.emreozcan.havadurumuuygulamasi.view

import android.Manifest
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.emreozcan.havadurumuuygulamasi.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import www.sanju.motiontoast.MotionToast


/**
 * Main Activity Navigation Bu aktiviteye bağlanmıştır
 * Konum burada alınır
 */
class MainActivity : AppCompatActivity(){
    var latitude =""
    var longitude =""

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var dialog : AlertDialog

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = this.getSharedPreferences("latlong", Context.MODE_PRIVATE)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (!isLocationEnabled(this)){
            var builder = AlertDialog.Builder(this)
            builder.setTitle("Konumunuz Kapalı")
            builder.setMessage("Uygulamayı kullanmak istiyorsanız lütfen konumunuzu açın")
            builder.setPositiveButton("Ayarlar"){dialog,which->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                finish()
            }
            builder.setCancelable(false)
            dialog = builder.create()
            dialog.show()
        }else{
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        location?.let {
                            latitude = location?.latitude.toString()
                            longitude = location?.longitude.toString()
                            sharedPreferences.edit().putString("latitude", latitude).apply()
                            sharedPreferences.edit().putString("longitude", longitude).apply()
                        }
                    }

            locationListener = object : LocationListener{
                override fun onLocationChanged(location: Location) {
                    if (location!=null){
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()
                        sharedPreferences.edit().putString("latitude", latitude).apply()
                        sharedPreferences.edit().putString("longitude", longitude).apply()
                    }

                }
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1f, locationListener)
            }

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode==1){
            if (grantResults.size>0){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    getLocationListener()
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0f, locationListener)
                    var intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle("İzin Gerekiyor")
                    builder.setMessage("Uygulamayı kullanmak istiyorsanız lütfen konum izni verin")
                    builder.setPositiveButton("Ayarlar"){ dialog, which->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri: Uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                        finish()
                    }
                    builder.setCancelable(false)
                    dialog = builder.create()
                    dialog.show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun getLocationListener(){
    }
    private fun isLocationEnabled(mContext: Context): Boolean {
        val lm = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)
    }
}
