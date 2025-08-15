package com.kaizencoder.newzify.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Route {

    @Serializable
    object HomeScreen : Route()

    @Serializable
    object SavedArticlesScreen : Route()

}
