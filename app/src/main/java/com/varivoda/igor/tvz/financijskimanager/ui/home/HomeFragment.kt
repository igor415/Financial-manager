package com.varivoda.igor.tvz.financijskimanager.ui.home


import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.databinding.FragmentHomeBinding
import com.varivoda.igor.tvz.financijskimanager.ui.menu.Menu
import com.varivoda.igor.tvz.financijskimanager.util.*
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber


class HomeFragment : Fragment() {

    private val homeViewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory((requireContext().applicationContext as App).productRepository)
    }
    private var _binding: FragmentHomeBinding? = null
    private var currentViewClicked: View? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createChannel()
        observeStockNotification()
        (activity as HomeActivity).setActionBarText(getString(R.string.home_title))
        listOf(statistics,customers,employees,insertInvoice,products,stores).forEach {
            registerForContextMenu(it)
        }
        //setTimerForDatabaseUpdate()
        statistics.setOnClickListener { onStatisticsClick() }
        customers.setOnClickListener { onCustomersClicked() }
        employees.setOnClickListener { onEmployeeClicked() }
        products.setOnClickListener { onProductsClicked() }
        insertInvoice.setOnClickListener { onInvoiceClicked() }
        stores.setOnClickListener { onStoresClicked() }
    }

    private fun observeStockNotification() {
        homeViewModel.stockDataNotification.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            when (it) {
                is NetworkResult.Success -> {
                    if (it.data) {
                        val manager =
                            requireActivity().getSystemService(NotificationManager::class.java)
                        manager.sendNotification(
                            getString(R.string.warehouse_status), requireContext(), getString(
                                R.string.notification_priority
                            )
                        )
                    }
                }
                else -> Timber.d("warehouse status - ok")
            }
            homeViewModel.stockDataNotification.value = null
        })
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

    fun onStatisticsClick() = findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(
        Menu.STATISTICS.string))

    fun onCustomersClicked() = findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(Menu.CUSTOMERS.string))

    fun onEmployeeClicked() = findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(Menu.EMPLOYEES.string))

    fun onInvoiceClicked() = findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(Menu.INSERT_BILL.string))

    fun onProductsClicked() = findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(Menu.PRODUCTS.string))

    fun onStoresClicked() = findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(Menu.STORES.string))


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