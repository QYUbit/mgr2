package com.qyub.mgr2.ui.screens.settings

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.qyub.mgr2.data.repo.EventRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    repo: EventRepository,
    onMenuRequest: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Settings")
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = TopAppBarDefaults.topAppBarColors().scrolledContainerColor,
                    navigationIconContentColor = TopAppBarDefaults.topAppBarColors().navigationIconContentColor,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = TopAppBarDefaults.topAppBarColors().actionIconContentColor
                ),
                navigationIcon = {
                    IconButton(
                        onClick = onMenuRequest
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            TextButton(
                onClick = {
                    scope.launch {
                        repo.allEvents()
                            .onEach { Log.d("Debug", it.toString()) }
                            .launchIn(this)
                    }
                }
            ) {
                Text("debug: log events")
            }

            TextButton(
                onClick = { }
            ) {
                Text("debug: delete all events", color = Color.Red)
            }
        }
    }
}

// D  [Event(id=1, title=Schlafen, description=null, isRepeating=false, repeatFor=WEEK_DAY, repeatAt=[0, 1, 2, 3, 4], date=2025-10-14, isAllDay=false, startTime=00:00, duration=440, isException=false, exceptionParentId=null, color=Color(0.078431375, 0.21176471, 0.9098039, 1.0, sRGB IEC61966-2.1), hasNotification=false, notificationMinutes=15, notificationType=REMINDER, createdAt=1760441735436, updatedAt=1760441735436), Event(id=2, title=Schlafen, description=null, isRepeating=false, repeatFor=WEEK_DAY, repeatAt=[6], date=2025-10-14, isAllDay=false, startTime=00:00, duration=630, isException=false, exceptionParentId=null, color=Color(0.078431375, 0.21176471, 0.9098039, 1.0, sRGB IEC61966-2.1), hasNotification=false, notificationMinutes=15, notificationType=REMINDER, createdAt=1760441782308, updatedAt=1760441782308)]