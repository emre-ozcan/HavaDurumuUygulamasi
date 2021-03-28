package com.emreozcan.havadurumuuygulamasi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.havadurumuuygulamasi.R
import com.emreozcan.havadurumuuygulamasi.adapter.CitiesRecyclerAdapter
import com.emreozcan.havadurumuuygulamasi.model.Locations
import com.emreozcan.havadurumuuygulamasi.service.WeatherAPI
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_cities.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cities Fragment Şehirler Burada Listelenmektedir
 */
class CitiesFragment : Fragment() , CitiesRecyclerAdapter.Listener{

    private lateinit var longitude :String  //Konum Verileri
    private lateinit var latitude : String

    private val BASE_URL = "https://www.metaweather.com/api/" //API Base

    private lateinit var locationFirstList : ArrayList<Locations>
    private lateinit var citiesRecyclerAdapter: CitiesRecyclerAdapter

    private lateinit var loadingDialog: LoadingDialog   //Yüklenme Dialog

    private var compositeDisposable : CompositeDisposable?= null    //RxJava

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cities, container, false)

        compositeDisposable = CompositeDisposable()
        /**
         * Navigation Gelen Verinin Karşılanması
         */
        arguments?.let {
            latitude = CitiesFragmentArgs.fromBundle(it).latitude
            longitude = CitiesFragmentArgs.fromBundle(it).longitude

        }

        loadingDialog = LoadingDialog(requireContext())
        loadingDialog.startDialog()

        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context)
        view.rvCities.layoutManager = layoutManager
        view.rvCities.setHasFixedSize(true)

        if(longitude!=null&&latitude!=null){
            loadLocations()

        }
        return view
    }

    private fun loadLocations(){

        val myUrl : String = "location/search/?lattlong=${latitude},${longitude}"

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build().create(WeatherAPI::class.java)

        compositeDisposable?.add(retrofit.getLocations(myUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse))

    }
    private fun handleResponse(locationListModels: List<Locations>){
        locationFirstList = ArrayList(locationListModels)
        locationFirstList?.let {

            val cityList = ArrayList<Locations>()

            for(city in locationFirstList){
                if (city.location_type == "City"){
                    cityList.add(city)
                }
            }

            cityList?.let {

                citiesRecyclerAdapter = CitiesRecyclerAdapter(cityList,listener = this@CitiesFragment)
                view?.rvCities?.adapter = citiesRecyclerAdapter

                loadingDialog.dismissDialog()

            }
        }

    }

    override fun cardItemClick(locations: Locations) {
        /**
         * Bu metot, Tıklanılan Şehrin Detaylarını Göstermek İçin Oluşturulmuştur
         */
        val action = CitiesFragmentDirections.actionCitiesFragmentToDetailsFragment(locations.woeid)
        view?.findNavController()?.navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()

    }

}
