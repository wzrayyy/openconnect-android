package com.wzray.openconnect.ui.theme

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.animations.NavHostAnimatedDestinationStyle
import com.ramcosta.composedestinations.spec.DestinationStyle

object SlideEnd : DestinationStyle.Animated() {
    private const val DURATION = 200

    override val enterTransition:
            AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = {
        slideInHorizontally(
            initialOffsetX = { 1000 }, animationSpec = tween(DURATION)
        )
    }
    override val exitTransition:
            AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = {
        slideOutHorizontally(
            targetOffsetX = { -1000 }, animationSpec = tween(DURATION)
        )
    }
    override val popEnterTransition:
            AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = {
        slideInHorizontally(
            initialOffsetX = { -1000 }, animationSpec = tween(DURATION)
        )
    }
    override val popExitTransition:
            AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = {
        slideOutHorizontally(
            targetOffsetX = { 1000 }, animationSpec = tween(DURATION)
        )
    }
}

object Fade : NavHostAnimatedDestinationStyle() {
    private const val DURATION = 175

    override val enterTransition:
            AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeIn(
            initialAlpha = 0f, animationSpec = tween(DURATION)
        )
    }
    override val exitTransition:
            AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOut(
            targetAlpha = 0f, animationSpec = tween(DURATION)
        )
    }
}
