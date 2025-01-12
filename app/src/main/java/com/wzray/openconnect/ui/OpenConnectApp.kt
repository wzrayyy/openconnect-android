package com.wzray.openconnect.ui

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.HomeRouteDestination
import com.ramcosta.composedestinations.generated.destinations.LogRouteDestination
import com.ramcosta.composedestinations.generated.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator
import com.ramcosta.composedestinations.utils.startDestination
import com.wzray.openconnect.R
import com.wzray.openconnect.ui.theme.OpenConnectTheme

enum class NavDestinations(
    @StringRes val title: Int,
    val route: DirectionDestinationSpec,
    val icon: ImageVector,
    val iconSelected: ImageVector
) {
    LOGS(
        title = R.string.logs,
        route = LogRouteDestination,
        icon = Icons.AutoMirrored.Outlined.Article,
        iconSelected = Icons.AutoMirrored.Filled.Article
    ),
    HOME(
        title = R.string.home,
        route = HomeRouteDestination,
        icon = Icons.Outlined.Home,
        iconSelected = Icons.Filled.Home
    ),
    SETTINGS(
        title = R.string.settings,
        route = SettingsScreenDestination,
        icon = Icons.Outlined.Settings,
        iconSelected = Icons.Filled.Settings
    ),
}

@Composable
private fun BottomBar(
    navController: NavHostController
) {
    val navigator = navController.rememberDestinationsNavigator()

    val currentRoute =
        navController.currentDestinationAsState().value ?: NavGraphs.root.startDestination

    NavigationBar {
        NavDestinations.entries.forEach { destination ->
            val isSelected = currentRoute == destination.route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navigator.navigate(destination.route) {
                        popUpTo(NavDestinations.HOME.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        if (isSelected) destination.iconSelected else destination.icon,
                        null
                    )
                },
                label = { Text(stringResource(destination.title)) },
            )
        }
    }
}

class RootDestinationsNavigator(value: DestinationsNavigator) : DestinationsNavigator by value

// This is *really* the only way to have pretty animations with bottom navbar...
@Destination<RootGraph>(
    start = true
)
@Composable
fun MainGraphNavHost(
    rootNavController: NavController
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomBar(navController)
        },
    ) { padding ->
        // compensate for Scaffold's smartness
        val bottomOffset = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
        val outerPadding = PaddingValues(bottom = padding.calculateBottomPadding() - bottomOffset)
        DestinationsNavHost(
            navGraph = NavGraphs.main,
            navController = navController,
            dependenciesContainerBuilder = {
                dependency(RootDestinationsNavigator(rootNavController.rememberDestinationsNavigator()))
                dependency(outerPadding)
            }
        )
    }
}


@Composable
fun OpenConnectApp() {
    val navHostController = rememberNavController()

    OpenConnectTheme {
        DestinationsNavHost(
            navGraph = NavGraphs.root,
            navController = navHostController,
        )
    }
}