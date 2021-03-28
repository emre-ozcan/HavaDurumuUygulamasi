package com.emreozcan.havadurumuuygulamasi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.havadurumuuygulamasi.model.Locations
import com.emreozcan.havadurumuuygulamasi.R
import kotlinx.android.synthetic.main.card_location_design.view.*

/**
 * Home Fragment Recycler View Adaptörü
 */
class HomeRecyclerAdapter(private val locationList : ArrayList<Locations> ) : RecyclerView.Adapter<HomeRecyclerAdapter.HomeHolder>() {

    class HomeHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun binding(locations : Locations){
            itemView.textViewTitle.text = locations.title
            itemView.textViewDistance.text = "Uzaklık: "+locations.distance.toString()
            itemView.textViewType.text = locations.location_type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_location_design,parent,false)
        return HomeHolder(view)
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        holder.binding(locationList[position])
    }

    override fun getItemCount(): Int {
        return  locationList.size
    }

}