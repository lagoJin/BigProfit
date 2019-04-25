package kr.meet.depro.bigprofit.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.tedpark.tedpermission.rx2.TedRx2Permission
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kr.meet.depro.bigprofit.R
import kr.meet.depro.bigprofit.adapter.PagerAdapter
import kr.meet.depro.bigprofit.api.APIInterface
import kr.meet.depro.bigprofit.api.ApiClient
import kr.meet.depro.bigprofit.base.BaseActivity
import kr.meet.depro.bigprofit.databinding.ActivityMainBinding
import kr.meet.depro.bigprofit.model.MarkerItem
import kr.meet.depro.bigprofit.model.Mart
import kr.meet.depro.bigprofit.model.Product
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main), OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    //https://github.com/umano/AndroidSlidingUpPanel

    private lateinit var map: GoogleMap
    private val markerList = ArrayList<Marker>()
    private lateinit var beforeMarker: Marker
    private lateinit var location: Location

    private val adapter by lazy { PagerAdapter(supportFragmentManager) }
    private var productList: ArrayList<Product> = arrayListOf()
    override fun initView() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            initPermission()
            return
        }
        initLocation()
        initViewPager()

        dataBinding.ivMainSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

    }

    override fun start() {
        val params: CoordinatorLayout.LayoutParams = dataBinding.appBar.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = AppBarLayout.Behavior()
        behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(p0: AppBarLayout): Boolean {
                return false
            }
        })
        params.behavior = behavior
    }

    ///region JinHo 상단 뷰
    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            this.map = it
            map.setOnMarkerClickListener(this)
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
            it.moveCamera(CameraUpdateFactory.newLatLng(LatLng(37.566535, 126.97796919000007)))
            it.animateCamera(CameraUpdateFactory.zoomTo(10f))

            this@MainActivity.map.setOnMyLocationButtonClickListener(object : GoogleMap.OnMyLocationButtonClickListener {
                override fun onMyLocationButtonClick(): Boolean {
                    //TODO 마커 새로 그려주기 주변
                    return false
                }
            })
        }
    }

    private fun initPermission() {
        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            CompositeDisposable().add(
                    TedRx2Permission.with(this)
                            .setRationaleTitle("위치권한").setRationaleMessage("앱을 이용하려면 위치권한이 필요합니다.")
                            .setPermissions(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                            .request()
                            .subscribe({ result ->
                                if (result.isGranted) {
                                    initLocation()
                                    initViewPager()
                                } else {
                                    finish()
                                }
                            }, { throwable -> })
            )
        }

    }

    private fun initLocation() {
        val mapFragment: SupportMapFragment =
                supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            initPermission()
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 7000, 10f, locationListener)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 7000, 10f, locationListener)
        ApiClient.kakaoApi.getMarts().enqueue(object : Callback<Mart> {
            override fun onResponse(call: Call<Mart>, response: Response<Mart>) {
                if (response.isSuccessful) {
                    Log.d("마트", response.body().toString())
                    response.body()?.let {
                        setMarkerItem(it.documents)
                    }

                }
            }

            override fun onFailure(call: Call<Mart>, t: Throwable) {

            }
        })
    }

    private val locationListener = object : LocationListener {
        @SuppressLint("MissingPermission")
        override fun onLocationChanged(location: Location?) {
            location?.let {
                if (::map.isInitialized) {
                    this@MainActivity.location = it
                    map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(it.latitude, it.longitude)))
                    map.animateCamera(CameraUpdateFactory.zoomTo(17f))
                }
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }

        override fun onProviderEnabled(provider: String?) {

        }

        override fun onProviderDisabled(provider: String?) {

        }
    }

    fun setMarkerItem(list: List<Mart.Document>) {
        val gsList = list.filter { it.place_name.contains("GS25") }
        val cuList = list.filter { it.place_name.contains("CU") }
        val sevenList = list.filter { it.place_name.contains("세븐일레븐") }

        gsList.forEach { addMarker(MarkerItem(it.y.toDouble(), it.x.toDouble()), "GS25") }
        cuList.forEach { addMarker(MarkerItem(it.y.toDouble(), it.x.toDouble()), "CU") }
        sevenList.forEach { addMarker(MarkerItem(it.y.toDouble(), it.x.toDouble()), "세븐일레븐") }
    }

    private fun addMarker(markerItem: MarkerItem, type: String) {
        val position = LatLng(markerItem.lat, markerItem.lon)
        var icon: BitmapDescriptor? = null
        when (type) {
            "GS25" -> icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_gs_basic)
            "CU" -> icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_cu_basic)
            "세븐일레븐" -> icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_seven_basic)
        }

        val marker = map.addMarker(
                MarkerOptions()
                        .position(position)
                        .icon(icon)
        )
        marker.tag = type
        markerList.add(marker)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMarkerClick(marker: Marker?): Boolean {
        var icon: BitmapDescriptor? = null
        var csColor:Int = R.color.cardview_dark_background
        if (::beforeMarker.isInitialized) {
            when {
                beforeMarker.tag == "GS25" -> icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_gs_basic)
                beforeMarker.tag == "CU" -> icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_cu_basic)
                beforeMarker.tag == "세븐일레븐" -> icon =
                        BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_seven_basic)
            }
            beforeMarker.setIcon(icon)
        }
        //클릭 했을 때
        marker?.let {
            if (csBar.visibility == View.GONE) csBar.visibility = View.VISIBLE
            when {
                marker.tag == "GS25" -> {
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_gs_click)
                    csColor = R.color.gsRed
                    //데이터 받고 어댑터 새로그려주기
                }
                marker.tag == "CU" -> {
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_cu_click)
                    csColor = R.color.cuPurple
                }
                marker.tag == "세븐일레븐" -> {
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_seven_click)
                    csColor = R.color.sevenGreen
                }
            }
            marker.setIcon(icon)
            dataBinding.csBar.setText(marker.tag.toString())
            dataBinding.csBar.setBackgroundColor(getColor(csColor))
            dataBinding.tabs.setIndicatorColorResource(csColor)
            dataBinding.tabs.setDividerColorResource(csColor)
            beforeMarker = marker
        }
        return true
    }

    //endregion

    //region Inhan 하단 뷰
    private fun initViewPager() {
        dataBinding.viewPager.adapter = adapter
        dataBinding.viewPager.offscreenPageLimit = 2
        dataBinding.tabs.shouldExpand = true
        dataBinding.tabs.setViewPager(viewPager)
    }
    var BaseURL:String ="https://jsonplaceholder.typicode.com"
    var retrofit = Retrofit.Builder()
        .baseUrl(BaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()
    var server = retrofit.create(APIInterface::class.java)

    fun productRequest(store:String, count:Int, event:Int, page:Int){
        server?.getRequest(store,count,event,page)?.enqueue(object : Callback<ArrayList<Product>>{
            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                Log.d("retrofit","실패")
            }

            override fun onResponse(call: Call<ArrayList<Product>>, response: Response<ArrayList<Product>>) {
                Log.d("retrofit","성공")
                if(response.isSuccessful && !response.body().isNullOrEmpty()){
                    productList.addAll(response.body()!!)
                }
            }
        })
    }


    //endregion
}
