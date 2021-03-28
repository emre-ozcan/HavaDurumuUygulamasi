package com.emreozcan.havadurumuuygulamasi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.havadurumuuygulamasi.R
import com.emreozcan.havadurumuuygulamasi.model.ConsolidatedWeather
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_weather_design.view.*

/**
 * Details Fragment Recycler View Adaptörü
 */
class DetailsRecyclerAdapter(private val weatherList : ArrayList<ConsolidatedWeather>) : RecyclerView.Adapter<DetailsRecyclerAdapter.DetailHolder>() {

    class DetailHolder(view : View) : RecyclerView.ViewHolder(view){
        fun binding(c: ConsolidatedWeather){
            itemView.textViewCardDay.text = c.applicableDate
            itemView.textViewCardMinMax.text
            itemView.textViewCardTemp.text = "${c.theTemp.toInt()}\u2103"
            itemView.textViewCardMinMax.text = "${c.minTemp.toInt()}\u2103 / ${c.maxTemp.toInt()}\u2103"
            itemView.textViewCardStatus.text = c.weatherStateName
            /**
             * Resimleri Yüklemek İçin Picasso Kütüphanesi Kullanılmıştır
             */
            val url = "https://www.metaweather.com/static/img/weather/png/${c.weatherStateAbbr}.png"
            Picasso.get().load(url)
                .placeholder(R.drawable.ic_loader)
                .into(itemView.imageViewCard)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_weather_design,parent,false)
        return DetailHolder(view)
    }

    override fun onBindViewHolder(holder: DetailHolder, position: Int) {
        holder.binding(weatherList[position])
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }
}