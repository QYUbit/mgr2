package com.qyub.mgr2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.qyub.mgr2.data.db.AppDatabase
import com.qyub.mgr2.data.repo.EventRepository
import com.qyub.mgr2.ui.screens.calendar.CalendarScreen
import com.qyub.mgr2.ui.screens.calendar.CalendarViewModel
import com.qyub.mgr2.ui.screens.calendar.CalendarViewModelFactory
import com.qyub.mgr2.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getInstance(applicationContext)
        val repo = EventRepository(db.eventDao())

        val factory = CalendarViewModelFactory(repo)

        val vm = ViewModelProvider(this, factory)[CalendarViewModel::class.java]

        setContent {
            AppTheme {
                CalendarScreen(vm)
            }
        }
    }
}