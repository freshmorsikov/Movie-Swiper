package com.github.freshmorsikov.moviematcher.app

import kotlinx.serialization.Serializable

sealed interface BottomNavigationRoute: NavigationRoute

interface NavigationRoute {

    @Serializable
    data object Swipe: BottomNavigationRoute

    @Serializable
    data object Favorite: BottomNavigationRoute

    @Serializable
    data object Matches: BottomNavigationRoute

}