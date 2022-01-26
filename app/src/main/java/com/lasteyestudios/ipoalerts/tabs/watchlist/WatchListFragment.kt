package com.lasteyestudios.ipoalerts.tabs.watchlist


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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lasteyestudios.ipoalerts.MainActivity
import com.lasteyestudios.ipoalerts.R
import com.lasteyestudios.ipoalerts.data.local.model.CompanyLocalModel
import com.lasteyestudios.ipoalerts.databinding.FragmentWatchListBinding
import com.lasteyestudios.ipoalerts.repository.NotificationRepo
import com.lasteyestudios.ipoalerts.tabs.current.ItemRecyclerAdapter


class WatchListFragment : Fragment() {

    private var _binding: FragmentWatchListBinding? = null
    private val binding get() = _binding!!

    private val notificationRepo = NotificationRepo.getInstance()

    private lateinit var itemRecyclerAdapter: ItemRecyclerAdapter
    private val watchListViewModel: WatchListViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWatchListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemRecyclerAdapter = ItemRecyclerAdapter(requireContext(), { searchId, growwShortName ->
            //todo
        }, { deleteGrowwShortName ->
            watchListViewModel.deleteCompanyWishlistByGrowwShortName(deleteGrowwShortName)
        }, { add ->
            watchListViewModel.insertWatchlistCompanyLocal(CompanyLocalModel(0,
                0,
                add.growwShortName.toString(),
                add))
        })


        watchListViewModel.getAllCompanyWishlist.observe(viewLifecycleOwner) {
            itemRecyclerAdapter.submitList(watchListViewModel.getListCompanyFromCompanyLocal(it))
        }
        binding.watchlistRecycler.adapter = itemRecyclerAdapter



        notificationRepo.notifications.observe(viewLifecycleOwner) { notifications ->
            if (notifications != null) {
                for (i in notifications.indices) {
                    if (notifications[i] != null) {
                        for (j in notifications[i]?.indices!!) {
                            notifications[i]?.get(j)?.let { notificationGroup("IPO Alerts", it) }
                        }
                    }
                }
            }
        }

//        binding.text.setOnClickListener {

//            findNavController().navigate(R.id.action_watchListFragment_to_listedFragment)
//        }

//        binding.text.setOnClickListener {
//
//            notificationGroup("hello","you bitch!!!!!")
//            notificationGroup("hello5454","you bit1ch!!!!!")
//
//        }
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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}