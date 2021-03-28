package com.emreozcan.havadurumuuygulamasi.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.emreozcan.havadurumuuygulamasi.R
import com.emreozcan.havadurumuuygulamasi.adapter.DetailsRecyclerAdapter
import com.emreozcan.havadurumuuygulamasi.model.ConsolidatedWeather
import com.emreozcan.havadurumuuygulamasi.model.WeatherJson
import com.emreozcan.havadurumuuygulamasi.service.WeatherAPI
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_details.view.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Details Fragment bu bir Bottom Sheet Dialog'dur
 * Şehirlerin toplamda 8 günlük hava durumu gösterilir
 */
class DetailsFragment : BottomSheetDialogFragment() {

    private var woeid: Int = 0  //Tıklanılan şehrin kendine has olan woeid'si

    private val BASE_URL = "https://www.metaweather.com/api/" //API base

    private lateinit var detailsRecyclerAdapter: DetailsRecyclerAdapter
    private lateinit var dateList: ArrayList<ConsolidatedWeather>
    private lateinit var listResult: ArrayList<ConsolidatedWeather>

    private var compositeDisposable: CompositeDisposable? = null
    private var compositeDisposableDays: CompositeDisposable? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_details, container, false)

        compositeDisposable = CompositeDisposable()
        compositeDisposableDays = CompositeDisposable()


        val layoutManager: RecyclerView.LayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
        view.rvDetail.layoutManager = layoutManager
        /**
         * Navigation gelen woeid karşılanmsası
         */
        arguments?.let {
            woeid = DetailsFragmentArgs.fromBundle(it).woeid
            loadWeather()

        }
        return view
    }

    private fun loadWeather() {
        val myUrl: String = "location/${woeid}/"

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build().create(WeatherAPI::class.java)

        compositeDisposable?.add(retrofit.getWeather(myUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleWeather))


    }
    private fun handleWeather(weatherJsonCall: WeatherJson){
        var weatherJson = weatherJsonCall
        listResult = ArrayList(weatherJson?.consolidatedWeather)

        val today = listResult[0]
        textViewCityWeather.text = weatherJson?.title
        textViewTempWeather.text = "${today.theTemp.toInt()}\u2103"
        textViewMinMaxWeather.text = "${today.minTemp.toInt()}\u2103 / ${today.maxTemp.toInt()}\u2103"
        textViewState.text = today.weatherStateName

        val url = "https://www.metaweather.com/static/img/weather/png/${today.weatherStateAbbr}.png"
        Picasso.get().load(url)
                .placeholder(R.drawable.ic_loader)
                .into(imageViewWeather)


        listResult.removeAt(0)
        listResult[0].applicableDate = "Yarın"

        detailsRecyclerAdapter = DetailsRecyclerAdapter(listResult)
        rvDetail.adapter = detailsRecyclerAdapter

        var day7String = getTime(today.applicableDate,6)
        getOtherDays("${weatherJson?.woeid}/$day7String")

        Handler(Looper.getMainLooper()).postDelayed({
            var day8String = getTime(today.applicableDate,7)
            getOtherDays("${weatherJson?.woeid}/$day8String")
        },300)



    }

    fun getTime(date: String, increment: Int): String {
        /**
         * Servis 7. ve 8. Günü Döndürmemektedir. Bu yüzden bu günler için servisten
         * başka bir sorgu yaptırmak gerekiyor bu yüzdende bugünün tarih verisini dönüştürmek
         * için bu metot oluşturulmuştur
         */
        var sdf = SimpleDateFormat("yyyy-MM-dd")
        var c = Calendar.getInstance()

        c.time = sdf.parse(date)
        c.add(Calendar.DATE, increment)

        var rString = sdf.format(c.time).replace('-', '/')
        println(rString)
        return rString
    }

    fun getOtherDays(dayUrl: String) {
        val myUrl: String = "location/$dayUrl"
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build().create(WeatherAPI::class.java)

        compositeDisposableDays?.add(retrofit.getDay(myUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleDays))
    }
    private fun handleDays(list : List<ConsolidatedWeather>){
        dateList = ArrayList(list)
        dateList?.let {
            listResult.add(dateList[0])
            detailsRecyclerAdapter.notifyDataSetChanged()

        }

    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()
        compositeDisposableDays?.clear()

    }
}
