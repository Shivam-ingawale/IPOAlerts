package com.lasteyestudios.ipoalerts.tabs.current

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.lasteyestudios.ipoalerts.MainActivity
import com.lasteyestudios.ipoalerts.R
import com.lasteyestudios.ipoalerts.data.local.model.CompanyLocalModel
import com.lasteyestudios.ipoalerts.data.models.Response
import com.lasteyestudios.ipoalerts.databinding.FragmentIpoBinding
import com.lasteyestudios.ipoalerts.repository.NotificationRepo
import com.lasteyestudios.ipoalerts.tabs.common.GetUpdatesWorker
import com.lasteyestudios.ipoalerts.tabs.common.SharedViewModel
import com.lasteyestudios.ipoalerts.tabs.watchlist.WatchListViewModel
import com.lasteyestudios.ipoalerts.utils.IPO_LOG_TAG
import java.util.concurrent.TimeUnit

class CurrentFragment : Fragment() {

    private var _binding: FragmentIpoBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAdapter: BlockRecyclerAdapter

    private lateinit var notificationRepo: NotificationRepo

    private val watchListViewModel: WatchListViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        periodicWork()
        watchListViewModel.loadData()
        notificationRepo = NotificationRepo.getInstance()

        _binding = FragmentIpoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        notificationGroup("he", "pop")

        mAdapter =
            BlockRecyclerAdapter(requireContext(), { searchId, growwShortName , liked ->
            findNavController().navigate(directions = CurrentFragmentDirections.actionCurrentFragmentToDetailsFragment2(
                searchId = searchId, growwShortName = growwShortName , liked= liked))
        }, { ipoCategory ->

            findNavController().navigate(CurrentFragmentDirections.actionCurrentFragmentToIpoCategory(
                ipoCategory = ipoCategory))

        }, { deleteSymbol ->
            watchListViewModel.deleteCompanyWishlistBySymbol(deleteSymbol)
        }, { add ->
            watchListViewModel.insertWatchlistCompanyLocal(CompanyLocalModel(0,
                System.currentTimeMillis() / 1000,
                add.growwShortName.toString(),
                add.symbol.toString(),
                add))
        })

        sharedViewModel.currentIPOs.observe(viewLifecycleOwner) { myResponse ->
            when (myResponse) {
                Response.Error -> {
                    handleRetry()
                }
                Response.Loading -> {

//                    Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {
                    binding.retryFab.visibility = View.INVISIBLE
//                    Log.d(IPO_LOG_TAG,
//                        "watchListViewModel.getAllGrowShortCompanyWishlist()" + watchListViewModel.getAllGrowShortCompanyWishlist)
                    for (j in myResponse.data.indices) {
                        if (myResponse.data?.get(j) != null) {
                            for (k in myResponse.data?.get(j)?.indices!!) {
                                if(!watchListViewModel.getAllSymbolCompanyWishlist.isEmpty()) {

                                    for (i in watchListViewModel.getAllSymbolCompanyWishlist.indices) {
                                        if (myResponse.data?.get(j)
                                                ?.get(k)?.symbol == watchListViewModel.getAllSymbolCompanyWishlist[i]
                                        ) {
                                            myResponse.data?.get(j)?.get(k)?.liked = true
                                            break
                                        } else {
                                            myResponse.data?.get(j)?.get(k)?.liked = false
                                        }
                                    }
                                }else{
                                    myResponse.data?.get(j)?.get(k)?.liked = false
                                }
                            }
                        }
                    }
                    mAdapter.submitList(myResponse.data)
                }
            }
        }

        binding.mainRecyclerView.adapter = mAdapter

    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }

    //    private var i = 0
    private fun periodicWork() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val updateRequest =
            PeriodicWorkRequestBuilder<GetUpdatesWorker>(4, TimeUnit.HOURS)
                .setConstraints(constraints)
                .addTag("updateRequest")
                .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "updateRequest",
            ExistingPeriodicWorkPolicy.REPLACE,
            updateRequest
        )

        val myWork: WorkRequest = updateRequest
        WorkManager.getInstance(requireContext()).enqueue(myWork)
        WorkManager.getInstance(requireContext())
            // requestId is the WorkRequest id
            .getWorkInfoByIdLiveData(updateRequest.id)
            .observe(viewLifecycleOwner) { workInfo: WorkInfo? ->
                if (workInfo != null) {

                    notificationRepo.notifications.observe(viewLifecycleOwner) { notifications ->
                        Log.d(IPO_LOG_TAG, "notifications reach with data ->$notifications")
                        if (notifications != null) {
                            for (i in notifications.indices) {
                                if (notifications[i] != null) {
                                    for (j in notifications[i]?.indices!!) {
                                        notifications[i]?.get(j)
                                            ?.let {
                                                notificationGroup("IPO Alerts", it)
                                                Log.d(IPO_LOG_TAG, "it -> $it")
                                            }
                                    }
                                }
                            }
                        }
                    }

                }
            }
    }

    private lateinit var notificationManager: NotificationManager
    private var summaryNotificationBuilder: NotificationCompat.Builder? = null
    private var bundleNotificationId = 100
    private var singleNotificationId = 100

    @SuppressLint("UnspecifiedImmutableFlag")
    fun notificationGroup(contentTitle: String, contentText: String) {

        //  Create Notification
//        val contentTitle = "contentTitle"
//        val contentText = "contentText"

        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification Group Key
        val groupKey = "bundle_notification_$bundleNotificationId"

        //  Notification Group click intent
        var resultIntent = Intent(requireContext(), MainActivity::class.java)
        resultIntent.putExtra("notification", "Summary Notification")
        resultIntent.putExtra("notification_id", bundleNotificationId)
        resultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        var resultPendingIntent = PendingIntent.getActivity(requireContext(),
            bundleNotificationId,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        // We need to update the bundle notification every time a new notification comes up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.notificationChannels.size < 2) {
                val groupChannel = NotificationChannel("bundle_channel_id",
                    "Bundles",
                    NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(groupChannel)
                val channel = NotificationChannel("channel_id",
                    "Reminders",
                    NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
            }
        }
        summaryNotificationBuilder = NotificationCompat.Builder(requireContext(),
            "bundle_channel_id")
            .setGroup(groupKey)
            .setGroupSummary(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(contentTitle)
            .setColor(ContextCompat.getColor(requireContext(), R.color.gold))
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
        if (singleNotificationId == bundleNotificationId) singleNotificationId =
            bundleNotificationId + 1 else singleNotificationId++

        //  Individual notification click intent
        resultIntent = Intent(requireContext(), MainActivity::class.java)
        resultIntent.putExtra("notification", "Single Notification")
        resultIntent.putExtra("notification_id", singleNotificationId)
        resultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        resultPendingIntent = PendingIntent.getActivity(requireContext(),
            singleNotificationId,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(requireContext(),
                "channel_id")
                .setGroup(groupKey)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setColor(ContextCompat.getColor(requireContext(), R.color.gold))
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setGroupSummary(false)
                .setContentIntent(resultPendingIntent)
        notificationManager.notify(singleNotificationId, notification.build())
        notificationManager.notify(bundleNotificationId, summaryNotificationBuilder!!.build())
    }


    private fun handleRetry() {
        binding.retryFab.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                Log.d(IPO_LOG_TAG, "handleRetry clicked")
                sharedViewModel.loadHomeIPOData()
            }
        }
//        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
    }
}