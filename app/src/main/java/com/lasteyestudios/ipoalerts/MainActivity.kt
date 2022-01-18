package com.lasteyestudios.ipoalerts

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.lasteyestudios.ipoalerts.databinding.ActivityMainBinding
import com.lasteyestudios.ipoalerts.tabs.common.SharedViewModel
import com.lasteyestudios.ipoalerts.utils.NetworkStatus


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
    }

    override fun onDestroy() {
        super.onDestroy()
        snackBar?.dismiss()
    }
}