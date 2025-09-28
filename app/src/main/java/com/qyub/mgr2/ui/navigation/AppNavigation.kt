package com.qyub.mgr2.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.qyub.mgr2.ui.components.AppDrawer
import com.qyub.mgr2.ui.screens.calendar.CalendarScreen
import com.qyub.mgr2.ui.screens.timeline.TimelineScreen
import com.qyub.mgr2.ui.screens.timeline.TimelineViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    timelineViewModel: TimelineViewModel
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                onRouteSelected = { route ->
                    navController.navigate(route.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.Timeline.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(NavigationRoutes.Timeline.route) {
                TimelineScreen(
                    vm = timelineViewModel,
                    onMenuRequest = { scope.launch {
                        drawerState.open()
                    }}
                )
            }

            composable(NavigationRoutes.Calendar.route) {
                CalendarScreen()
            }
        }
    }
}
