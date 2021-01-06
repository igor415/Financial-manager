package com.varivoda.igor.tvz.financijskimanager.ui.maps

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.beust.klaxon.*
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
import com.google.android.gms.maps.model.PolylineOptions
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.ui.settings.SettingsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import timber.log.Timber
import java.net.URL

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
            R.id.showClosest -> {
                buildPath()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun buildPath() {
        val options = PolylineOptions()
        options.color(Color.RED)
        options.width(5f)
        val url = getURL(LatLng(45.813019, 15.966568), LatLng(45.803019, 15.860000))
        lifecycleScope.launch(Dispatchers.IO){
            val result = URL(url).readText()
            withContext(Dispatchers.Main){
                val parser: Parser = Parser()
                val stringBuilder: StringBuilder = StringBuilder(result)
                val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                val routes = json.array<JsonObject>("routes")
                val points = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>
                //val polypts = points.map { it.obj("polyline")?.string("points")!!  }
                val polypts = points.flatMap { decodePoly(it.obj("polyline")?.string("points")!!)  }
                options.add(LatLng(45.813019, 15.966568))

                for (point in polypts) options.add(point)
                options.add(LatLng(45.803019, 15.860000))
                map.addPolyline(options)
                //map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

            }
        }
    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }

    private fun getURL(from : LatLng, to : LatLng) : String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val params = "$origin&$dest&$sensor"
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
    }

    fun setBrightness(float: Float){
        val lp = window.attributes
        lp.screenBrightness = float
        window.attributes = lp
    }

}