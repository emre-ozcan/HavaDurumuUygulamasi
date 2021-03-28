package com.emreozcan.havadurumuuygulamasi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.havadurumuuygulamasi.model.Locations
import com.emreozcan.havadurumuuygulamasi.R
import kotlinx.android.synthetic.main.card_cities_design.view.*

/**
 * Cities Fragment Recycler View Adaptörü
 */
class CitiesRecyclerAdapter(private val citiesList : ArrayList<Locations>, private val listener : Listener) : RecyclerView.Adapter<CitiesRecyclerAdapter.CitiesHolder>() {
    interface Listener{
        fun cardItemClick(locations: Locations)
    }
    class CitiesHolder(view : View) : RecyclerView.ViewHolder(view){
        fun binding(locations : Locations, listener : Listener){
            itemView.textViewCity.text = locations.title
            itemView.setOnClickListener{listener.cardItemClick(locations)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_cities_design,parent,false)
        return CitiesHolder(view)
    }

    override fun onBindViewHolder(holder: CitiesHolder, position: Int) {
        holder.binding(citiesList[position],listener)
    }

    override fun getItemCount(): Int {
        return citiesList.size
    }
}