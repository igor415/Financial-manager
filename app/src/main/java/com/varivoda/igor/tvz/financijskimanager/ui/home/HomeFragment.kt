package com.varivoda.igor.tvz.financijskimanager.ui.home

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.databinding.FragmentHomeBinding
import com.varivoda.igor.tvz.financijskimanager.ui.maps.MapsActivity
import com.varivoda.igor.tvz.financijskimanager.ui.menu.Menu
import com.varivoda.igor.tvz.financijskimanager.util.*
import com.varivoda.igor.tvz.financijskimanager.workmanager.BroadcastNotification
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setListeners()
        (activity as HomeActivity).setActionBarText("Home")
        createChannel()
        setTimerForDatabaseUpdate()
    }

    private fun setListeners() {
        if(this::navController.isInitialized){
            statistics.setOnClickListener {
                navController.navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(Menu.STATISTICS.string))
            }
            customers.setOnClickListener {
                navController.navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(Menu.CUSTOMERS.string))
            }
            employees.setOnClickListener {
                navController.navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(Menu.EMPLOYEES.string))
            }
            insertInvoice.setOnClickListener {
                navController.navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(Menu.INSERT_BILL.string))
            }
            products.setOnClickListener {
                navController.navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(Menu.PRODUCTS.string))
            }
            stores.setOnClickListener {
                startActivity(Intent(activity,MapsActivity::class.java))
                //navController.navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(Menu.STORES.string))
            }

        }else{
            context?.toast("There is a problem with navigation")
        }

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


    private fun setTimerForDatabaseUpdate(){
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
    }

}