package com.qyub.mgr2

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getInstance(applicationContext)
        val eventRepository = EventRepository(db.eventDao())
        val timelineViewModelFactory = TimelineViewModelFactory(eventRepository)

        timelineViewModel = ViewModelProvider(this, timelineViewModelFactory)[TimelineViewModel::class.java]

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppTheme {
                val isDarkTheme = isSystemInDarkTheme()

                LaunchedEffect(isDarkTheme) {
                    val controller = WindowInsetsControllerCompat(window, window.decorView)

                    controller.isAppearanceLightNavigationBars = !isDarkTheme

                    window.navigationBarColor = if (isDarkTheme) {
                        Color.TRANSPARENT
                    } else {
                        Color.TRANSPARENT
                    }
                }

                AppNavigation(timelineViewModel)
            }
        }
    }
}