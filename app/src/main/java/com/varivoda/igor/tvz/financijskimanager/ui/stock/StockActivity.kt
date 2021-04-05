package com.varivoda.igor.tvz.financijskimanager.ui.stock

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.model.ProductStockDTO
import com.varivoda.igor.tvz.financijskimanager.util.closeKeyboard
import com.varivoda.igor.tvz.financijskimanager.util.openKeyboard
import com.varivoda.igor.tvz.financijskimanager.util.toast
import kotlinx.android.synthetic.main.fragment_stock.*
import kotlinx.android.synthetic.main.header_dark.*
import kotlinx.android.synthetic.main.info_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class StockActivity : AppCompatActivity() {

    private val viewModel by viewModels<StockViewModel> {
        StockViewModelFactory((this.applicationContext as App).storeRepository)
    }
    private lateinit var stockAdapter: StockAdapter
    private var infoDialog: AlertDialog? = null
    private var currentProductName: String? = null
    private var phonePermission = false
    private var isShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock)
        stockAdapter = StockAdapter(showInfo, false)
        product_recycler.adapter = stockAdapter
        observeStockData()
        searchFeature()
        observeInfoResult()
        mSensorManager = this.getSystemService(Activity.SENSOR_SERVICE) as SensorManager
        /*Objects.requireNonNull(mSensorManager).registerListener(
            mSensorListener, mSensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER
            ), SensorManager.SENSOR_DELAY_NORMAL
        )*/
    }


    fun getAllInfo(code: Int, data: Intent) {
        if (code == REQ_CODE_SPEECH_INPUT) {
            val result = data
                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val listOfProducts = viewModel.allProducts.value ?: listOf()
            outer_loop@ for (i in 0 until result?.size!!) {
                val speechString = result[i]
                if (listOfProducts.isNotEmpty()) {
                    for (j in listOfProducts.indices) {
                        val product = listOfProducts[j]
                        if (speechString.toLowerCase(Locale.getDefault()) in product.productName.toLowerCase(
                                Locale.getDefault()
                            )
                        ) {
                            searchEditText.setText(speechString)
                            break@outer_loop
                        }
                    }
                }
            }
            if (searchEditText.text.isEmpty() && result.size > 0) searchEditText.setText(
                result[0]
            )
            searchEditText.setSelection(searchEditText.text.length)
        }
    }

    fun setActionBarText(text: String){
        supportActionBar?.elevation = 2f
        supportActionBar?.title = text
    }

    fun removeActionBar(){
        supportActionBar?.hide()
    }

    fun showActionBar(){
        supportActionBar?.show()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        removeActionBar()
        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            searchEditText.setText("")
            if (resultCode == Activity.RESULT_OK && null != data) {
                getAllInfo(REQ_CODE_SPEECH_INPUT, data)
            }
        }
        isShown = false
    }

    private fun observeInfoResult() {
        viewModel.info.observe(this, androidx.lifecycle.Observer {
            if (it == null) return@Observer
            infoDialog(it)
        })
    }

    private val showInfo: (ProductStockDTO) -> Unit = { item ->
        viewModel.getInfo(item.productId)
        currentProductName = item.productName
    }

    override fun onStart() {
        super.onStart()
        removeActionBar()
        /*mSensorManager.registerListener(
            mSensorListener,
            mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_UI
        )*/
    }

    override fun onStop() {
        super.onStop()
        //mSensorManager.unregisterListener(mSensorListener)
    }



    private fun searchFeature() {
        searchButton.setOnClickListener {
            if (searchEditText.visibility == View.VISIBLE) {
                searchButton.setImageResource(R.drawable.ic_search_gray)
                searchEditText.setText("")
                searchEditText.closeKeyboard(this)
                searchEditText.visibility = View.GONE
            } else {
                searchMode()
            }

        }
    }

    private fun searchMode() {
        searchButton.setImageResource(R.drawable.ic_close_gray)
        searchEditText.visibility = View.VISIBLE
        searchEditText.requestFocus()
        searchEditText.openKeyboard(this)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length > 1) {
                    val editedText = s.toString().toLowerCase(Locale.getDefault())
                    viewModel.filter.value = editedText
                } else if (s != null && s.isEmpty()) {
                    viewModel.filter.value = ""
                }
            }
        })
    }

    private fun observeStockData() {
        viewModel.allProducts.observe(this, androidx.lifecycle.Observer {
            if (it == null) return@Observer
            stockAdapter.submitList(it)
            println("debug data is $it")
            progress_bar.visibility = View.GONE
        })
    }

    private fun infoDialog(it: List<ProductStockDTO>) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.info_dialog, null, false)
        dialogView.product_popup_title.text = currentProductName ?: ""
        dialogView.ok_button.setOnClickListener {
            infoDialog?.cancel()
        }
        val adapter = StockAdapter(callStore, true)
        dialogView.infoList.layoutManager = LinearLayoutManager(this)
        dialogView.infoList.adapter = adapter
        adapter.submitList(it)
        builder.setView(dialogView)
        infoDialog?.cancel()
        infoDialog = builder.create()
        infoDialog?.show()

    }

    private val callStore: (ProductStockDTO) -> Unit = { item ->
        askForPermission()
        if (phonePermission) {
            lifecycleScope.launch(Dispatchers.IO) {
                val number = viewModel.getNumberForStoreId(item.idStore)
                withContext(Dispatchers.Main) {
                    if (number != null) {
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse("tel:$number")
                        startActivity(callIntent)
                    } else {
                        applicationContext.toast("There is problem with phone number.")
                    }

                }
            }
        } else {
            this.toast("You need to give permission for phone call.")
        }


    }

    private fun askForPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_PHONE_PERMISSION
            )
        } else {
            phonePermission = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PHONE_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                askForPermission()
            }
        }
    }

    var mAccelerometer = 10f
    var mAccelerometerCurrent = SensorManager.GRAVITY_EARTH
    var mAccelerometerLast = SensorManager.GRAVITY_EARTH
    lateinit var mSensorManager: SensorManager

   /* val mSensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            mAccelerometerLast = mAccelerometerCurrent
            mAccelerometerCurrent =
                Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = mAccelerometerCurrent - mAccelerometerLast
            mAccelerometer = mAccelerometer * 0.9f + delta
            if (mAccelerometer > 11) {
                if (!isShown) {
                    SpeechService.promptSpeechInput(
                        REQ_CODE_SPEECH_INPUT,
                        applicationContext,
                        this@StockActivity
                    )
                    isShown = true
                }

            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    }*/


}