package com.qyub.mgr2.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationRoutes(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Timeline : NavigationRoutes("timeline", "Timeline", Icons.Filled.Home)
    data object Calendar : NavigationRoutes("calendar", "Calendar", Icons.Filled.DateRange)

    companion object {
        val allRoutes by lazy { listOf(Timeline, Calendar) }
    }
}

