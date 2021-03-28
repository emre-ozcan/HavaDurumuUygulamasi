package com.emreozcan.havadurumuuygulamasi.view

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.emreozcan.havadurumuuygulamasi.R
import com.emreozcan.havadurumuuygulamasi.view.StartFragmentDirections


class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Handler().postDelayed({
            val action = StartFragmentDirections.actionStartFragmentToHomeFragment()
            view.findNavController().navigate(action)
        },3000)

        super.onViewCreated(view, savedInstanceState)
    }

}