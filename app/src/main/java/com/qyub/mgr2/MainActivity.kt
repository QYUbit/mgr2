package com.qyub.mgr2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.qyub.mgr2.data.db.AppDatabase
import com.qyub.mgr2.data.repo.EventRepository
import com.qyub.mgr2.ui.screens.timeline.TimelineScreen
import com.qyub.mgr2.ui.screens.timeline.TimelineViewModel
import com.qyub.mgr2.ui.screens.timeline.TimelineViewModelFactory
import com.qyub.mgr2.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getInstance(applicationContext)
        val repo = EventRepository(db.eventDao())

        val factory = TimelineViewModelFactory(repo)

        val vm = ViewModelProvider(this, factory)[TimelineViewModel::class.java]

        setContent {
            AppTheme {
                TimelineScreen(vm)
            }
        }
    }
}