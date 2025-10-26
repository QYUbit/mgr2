package com.qyub.mgr2

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.qyub.mgr2.data.db.AppDatabase
import com.qyub.mgr2.data.repo.EventRepository
import com.qyub.mgr2.ui.navigation.AppNavigation
import com.qyub.mgr2.ui.screens.timeline.TimelineViewModel
import com.qyub.mgr2.ui.screens.timeline.TimelineViewModelFactory
import com.qyub.mgr2.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    private lateinit var timelineViewModel: TimelineViewModel

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        val db = AppDatabase.getInstance(applicationContext)
        val eventRepository = EventRepository(db.eventDao(), applicationContext)
        val timelineViewModelFactory = TimelineViewModelFactory(eventRepository)

        timelineViewModel = ViewModelProvider(this, timelineViewModelFactory)[TimelineViewModel::class.java]

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppTheme {
                val isDarkTheme = isSystemInDarkTheme()

                LaunchedEffect(isDarkTheme) {
                    val controller = WindowInsetsControllerCompat(window, window.decorView)
                    controller.isAppearanceLightNavigationBars = !isDarkTheme
                    window.navigationBarColor = Color.TRANSPARENT
                }

                AppNavigation(eventRepository, timelineViewModel)
            }
        }
    }
}