package com.varivoda.igor.tvz.financijskimanager.ui.home

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.databinding.FragmentHomeBinding
import com.varivoda.igor.tvz.financijskimanager.ui.maps.MapsActivity
import com.varivoda.igor.tvz.financijskimanager.ui.menu.Menu
import com.varivoda.igor.tvz.financijskimanager.util.*
import com.varivoda.igor.tvz.financijskimanager.workmanager.BroadcastNotification
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var binding: FragmentHomeBinding
    private var currentViewClicked: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModelFactory = HomeViewModelFactory(findNavController())
        homeViewModel = ViewModelProvider(requireActivity(),homeViewModelFactory).get(HomeViewModel::class.java)
        binding.viewModel = homeViewModel
        //(activity as HomeActivity).setActionBarText(getString(R.string.home_title))
        createChannel()
        listOf(statistics,customers,employees,insertInvoice,products,stores).forEach {
            registerForContextMenu(it)
        }
        //setTimerForDatabaseUpdate()
    }


    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.add("Disable")
        currentViewClicked = v
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if(item.title == "Disable"){
            if(currentViewClicked != null) {
                currentViewClicked?.isEnabled = !currentViewClicked?.isEnabled!!
                currentViewClicked?.alpha = if (currentViewClicked!!.isEnabled) 1.0f else 0.6f
            }
        }
        return super.onContextItemSelected(item)
    }


    private fun createChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = CHANNEL_DESCRIPTION

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }


   /* private fun setTimerForDatabaseUpdate(){
        val alarmFor: Calendar = Calendar.getInstance()
        alarmFor.set(Calendar.HOUR_OF_DAY, 15)
        alarmFor.set(Calendar.MINUTE, 40)
        alarmFor.set(Calendar.SECOND, 0)

        val i = Intent(
            requireContext().applicationContext,
            BroadcastNotification::class.java
        )
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext().applicationContext,
            0,
            i,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val timer =
            context?.getSystemService(ALARM_SERVICE) as AlarmManager?

        timer!!.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmFor.timeInMillis,
            pendingIntent
        )
    }*/

}