package com.emreozcan.havadurumuuygulamasi.view

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.emreozcan.havadurumuuygulamasi.R

class LoadingDialog(var context: Context) {
    private lateinit var dialog : AlertDialog
    fun startDialog(){
        var builder = AlertDialog.Builder(context)
        var inflater : LayoutInflater = LayoutInflater.from(context)

        builder.setView(inflater.inflate(R.layout.custom_alert_dialog,null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }


}