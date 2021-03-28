package com.emreozcan.havadurumuuygulamasi.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.havadurumuuygulamasi.R
import com.emreozcan.havadurumuuygulamasi.adapter.HomeRecyclerAdapter
import com.emreozcan.havadurumuuygulamasi.model.Locations
import com.emreozcan.havadurumuuygulamasi.service.WeatherAPI
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import www.sanju.motiontoast.MotionToast

/**
 * Home Fragment
 * Bulunulan konuma yakın olan lokasyonlar burada listelenmektedir
 */
class HomeFragment : Fragment() , LocationListener{
    private lateinit var homeRecyclerAdapter: HomeRecyclerAdapter

    var latitude =""
    var longitude =""

    val konumSaglayici  = "gps"

    private lateinit var locationManager: LocationManager

    private val BASE_URL = "https://www.metaweather.com/api/" //API Base

    private lateinit var locationList : ArrayList<Locations>

    private lateinit var loadingDialog: LoadingDialog

    private var compositeDisposable : CompositeDisposable?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context)
        view.rvHome.layoutManager = layoutManager

        compositeDisposable = CompositeDisposable()

        loadingDialog = LoadingDialog(requireContext())
        loadingDialog.startDialog()

        getLocation()
        hideFab(view)

        view.fab.setOnClickListener(View.OnClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToCitiesFragment(latitude, longitude)
                view.findNavController().navigate(action)
        })

        return view
    }

    private fun getLocation(){
        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
            if (activity?.let { ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.ACCESS_FINE_LOCATION) } == true) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }else{

            var konum = locationManager.getLastKnownLocation(konumSaglayici)
            if (konum!=null){
                onLocationChanged(konum)
                loadLocations()
            }else{
                MotionToast.createColorToast(requireActivity(),
                        "Hata ! Konumunuz Kapalı Olabİlİr",
                        "Lütfen Konumunuzu Kontrol Ediniz!",
                        MotionToast.TOAST_ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(requireActivity(),R.font.helvetica_regular))
            }
        }

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
        locationList = ArrayList(locationListModels)
        locationList?.let {
            homeRecyclerAdapter = HomeRecyclerAdapter(locationList)
            rvHome.adapter = homeRecyclerAdapter
            loadingDialog.dismissDialog()
        }
    }
    private fun hideFab(view: View){
        /**
         * Bu metot Fabın Recycler Scroll edildiğinde kaybolarak daha iyi bir deneyim elde
         * edilmesi için oluşturulmuştur
         */
        view.rvHome.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy<0&&!view.fab.isShown){
                    view.fab.show()
                }else if (dy>0&&view.fab.isShown){
                    view.fab.hide()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }
    override fun onLocationChanged(location: Location) {
        latitude = location.latitude.toString()
        longitude = location.longitude.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }

}