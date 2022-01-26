package com.lasteyestudios.ipoalerts

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.lasteyestudios.ipoalerts.databinding.ActivityMainBinding
import com.lasteyestudios.ipoalerts.tabs.common.GetUpdatesWorker
import com.lasteyestudios.ipoalerts.tabs.common.SharedViewModel
import com.lasteyestudios.ipoalerts.utils.IPO_LOG_TAG
import com.lasteyestudios.ipoalerts.utils.NetworkStatus
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    //    private var currentNavController: LiveData<NavController>? = null
    private lateinit var currentNavController: NavController
    private var snackBar: Snackbar? = null
    private lateinit var binding: ActivityMainBinding

    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var networkStatus: NetworkStatus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        networkStatus = NetworkStatus(this).apply {
            startNetworkCallback {
                if (!it) {
                    snackBar = Snackbar.make(binding.bottomNavView,
                        "No network connection!",
                        Snackbar.LENGTH_LONG)
                    snackBar?.setAction("DISMISS") { snackBar?.dismiss() }
                        ?.setAnchorView(binding.bottomNavView)
                        ?.show()
                } else snackBar?.dismiss()
            }
        }

        sharedViewModel.loadHomeIPOData()
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        currentNavController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(currentNavController)

        networkStatus = NetworkStatus(this).apply {
            if (!getCurrentNetworkStatus()) {
                Toast.makeText(applicationContext, "No network connection!", Toast.LENGTH_LONG)
                    .show()
            }
        }
//        periodicWork()
        Log.d(IPO_LOG_TAG, "in main activity")
    }


//    private var i = 0
    private fun periodicWork() {
//        if (i > 1) {
//            return
//        }
//        i++
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        val alreadyRated = sharedPref.getBoolean(getString(R.string.worker_started), false)
//        val alreadyRated = false

        if (alreadyRated) {
            return
        }

        val workConstraint =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(GetUpdatesWorker::class.java, 16, TimeUnit.MINUTES)
                .setConstraints(workConstraint)
                .build()

        // for test
//        val once = OneTimeWorkRequest.Builder(GetUpdatesWorker::class.java).build()
        val workManager = WorkManager.getInstance(applicationContext)
//        Log.d(IPO_LOG_TAG, "once done")
//        workManager.enqueue(once)
        workManager.enqueue(periodicWorkRequest)

        with(sharedPref.edit()) {
            putBoolean(getString(R.string.worker_started), true)
            apply()
            }
//
//        workManager.getWorkInfoByIdLiveData(once.id)
        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(this@MainActivity) {
                if (it.state.isFinished) {

                }
            }

    }


    override fun onDestroy() {
        super.onDestroy()
        snackBar?.dismiss()
    }
}