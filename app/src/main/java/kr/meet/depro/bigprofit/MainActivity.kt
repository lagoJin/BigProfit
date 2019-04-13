package kr.meet.depro.bigprofit

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import com.depromeet.dog1plus1benefit.pagerAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.tedpark.tedpermission.rx2.TedRx2Permission
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kr.meet.depro.bigprofit.api.ApiClient
import kr.meet.depro.bigprofit.base.BaseActivity
import kr.meet.depro.bigprofit.databinding.ActivityMainBinding
import kr.meet.depro.bigprofit.model.Mart
import kr.meet.depro.bigprofit.model.product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    //https://github.com/umano/AndroidSlidingUpPanel

    private lateinit var map: GoogleMap
    private val markerList = ArrayList<Marker>()
    private lateinit var beforeMarker: Marker

    private val adapter by lazy{ pagerAdapter(supportFragmentManager) }
    private var productList = mutableListOf<product>()
    override fun initView() {
        initPermission()
        initLocation()
        initViewPager()
    }

    override fun start() {

    }

    ///region JinHo 상단 뷰
    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            this.map = it
            map.setOnMarkerClickListener(this)
            it.isMyLocationEnabled = true
            it.uiSettings.isMyLocationButtonEnabled = true
            it.moveCamera(CameraUpdateFactory.newLatLng(LatLng(37.566535, 126.97796919000007)))
            it.animateCamera(CameraUpdateFactory.zoomTo(10f))
        }
    }

    private fun initPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
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
                    .subscribe(
                        { result ->
                            if (result.isGranted) {
                                Toast.makeText(this, "위치권한이 승인 되었습니다.", Toast.LENGTH_SHORT).show()

                            } else {
                                finish()
                            }
                        },
                        { throwable -> })
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 10f, locationListener)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10f, locationListener)
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
                    map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude, location.longitude)))
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

    override fun onMarkerClick(marker: Marker?): Boolean {
        var icon: BitmapDescriptor? = null
        if (::beforeMarker.isInitialized) {
            when {
                beforeMarker.tag == "GS25" -> icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_gs_basic)
                beforeMarker.tag == "CU" -> icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_cu_basic)
                beforeMarker.tag == "세븐일레븐" -> icon =
                    BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_seven_basic)
            }
            beforeMarker.setIcon(icon)
        }
        marker?.let {
            when {
                marker.tag == "GS25" -> icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_gs_click)
                marker.tag == "CU" -> icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_cu_click)
                marker.tag == "세븐일레븐" -> icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_seven_click)
            }
            marker.setIcon(icon)
            beforeMarker = marker
        }
        return true
    }

    //endregion

    //region Inhan 하단 뷰
    fun initViewPager(){
        viewPager.adapter = MainActivity@adapter
        viewPager.offscreenPageLimit = 3
        tabs.shouldExpand = true

        tabs.setViewPager(viewPager)

    }

    //endregion
}
