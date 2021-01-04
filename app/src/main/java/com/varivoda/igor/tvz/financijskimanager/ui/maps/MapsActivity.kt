package com.varivoda.igor.tvz.financijskimanager.ui.maps

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.ui.settings.SettingsActivity

import timber.log.Timber

const val REQUEST_LOCATION_PERMISSION = 1
const val REQUEST_TURN_DEVICE_LOCATION_ON = 2

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        actionBar?.title = getString(R.string.stores_locations)
        val pref = Preferences(this)
        /*setBrightness(pref.getSeekBarValue())*/
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        askForPermission()
        checkDeviceLocationSettings()
        showStoresLocations(map)
        setPoiClick(map)
    }


    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            poiMarker.showInfoWindow()
        }
    }

    private fun askForPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }else{
            map.isMyLocationEnabled = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_LOCATION_PERMISSION){
            if(grantResults.contains(PackageManager.PERMISSION_GRANTED)){
                askForPermission()
            }
        }
    }

    private fun showStoresLocations(map: GoogleMap) {
        val coordinates = mapOf(LatLng(45.813019, 15.966568) to "Ilica",LatLng(45.803019, 15.860000) to "Jankomir"
                                ,LatLng(45.777019, 15.966568) to "Novi Zagreb",LatLng(45.777019, 16.057568) to "Žitnjak"
                                ,LatLng(45.328979, 14.457664) to "Rijeka",LatLng(45.5511111, 18.6938889) to "Osijek")
        val zoomLevel = 10.0f

        coordinates.forEach {
            map.addMarker(MarkerOptions().position(it.key).title(it.value).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).showInfoWindow()
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates.filterValues { it == "Novi Zagreb"}.keys.first(), zoomLevel))
    }



    private fun checkDeviceLocationSettings() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(this)
        val locationSettingsResponseTask =
            settingsClient.checkLocationSettings(builder.build())
        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                try {
                    exception.startResolutionForResult(this,
                        REQUEST_TURN_DEVICE_LOCATION_ON)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Timber.d("Error getting location settings resolution: ${sendEx.message}")
                }
            } else {
                showSnackBar()
            }
        }
        locationSettingsResponseTask.addOnCompleteListener {

        }
    }
   /* inline val Activity.contentView: View?
        get() = findOptional<ViewGroup>(android.R.id.content)?.getChildAt(0)*/

    private fun showSnackBar() {

        /*applicationContext!!.findViewById(android.R.id.content)  .getSnackBar("uk")?.setAction(android.R.string.ok){
            checkDeviceLocationSettings()
        }?.show()*/

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.satelliteMap -> {
                map.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.normalMap -> {
                map.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    fun setBrightness(float: Float){
        val lp = window.attributes
        lp.screenBrightness = float
        window.attributes = lp
    }

}