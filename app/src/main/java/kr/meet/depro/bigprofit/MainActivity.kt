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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.tedpark.tedpermission.rx2.TedRx2Permission
import io.reactivex.disposables.CompositeDisposable
import kr.meet.depro.bigprofit.api.ApiClient
import kr.meet.depro.bigprofit.base.BaseActivity
import kr.meet.depro.bigprofit.databinding.ActivityMainBinding
import kr.meet.depro.bigprofit.model.Mart
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun initView() {
        initPermission()
        initLocation()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            this.map = it
            it.isMyLocationEnabled = true
            it.uiSettings.isMyLocationButtonEnabled = true
            it.moveCamera(CameraUpdateFactory.newLatLng(LatLng(37.566535, 126.97796919000007)))
            it.animateCamera(CameraUpdateFactory.zoomTo(10f))
        }
    }

    private fun initPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            CompositeDisposable().add(
                    TedRx2Permission.with(this)
                            .setRationaleTitle("위치권한").setRationaleMessage("앱을 이용하려면 위치권한이 필요합니다.")
                            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
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

    override fun start() {

    }

    private fun initLocation() {
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            initPermission()
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 10f, locationListener)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10f, locationListener)
        ApiClient.kakaoApi.getMarts().enqueue(object : Callback<Mart> {
            override fun onResponse(call: Call<Mart>, response: Response<Mart>) {
                if (response.isSuccessful) {

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

}
