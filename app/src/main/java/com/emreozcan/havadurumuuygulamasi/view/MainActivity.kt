package com.emreozcan.havadurumuuygulamasi.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.emreozcan.havadurumuuygulamasi.R
import kotlinx.android.synthetic.main.activity_main.*
import www.sanju.motiontoast.MotionToast

/**
 * Main Activity Navigation Bu aktiviteye bağlanmıştır
 */
class MainActivity : AppCompatActivity() {
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray){
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                        MotionToast.createColorToast(this,
                            "Ayarlamalar Yapılıyor",
                            "Uygulama Yeniden Başlatılıyor",
                            MotionToast.TOAST_SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this,R.font.helvetica_regular))
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    MotionToast.createColorToast(this,
                        "İzİn Reddedİldİ",
                        "Uygulamayı Kullanmak İçin Lokasyon İzni Gereklidir",
                        MotionToast.TOAST_ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this,R.font.helvetica_regular))
                    onBackPressed()
                }
                return
            }
        }
    }
}
