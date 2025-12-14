package com.example.workmate_test.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

typealias BackStack = NavBackStack<NavKey>

fun BackStack.pop() = removeLastOrNull()

fun BackStack.isBackNavigationAllowed(): Boolean {
    val lastTwoRoutes = takeLast(2)
    return lastTwoRoutes.size == 2
            && lastTwoRoutes[0] is Route.CountriesRoute
            && lastTwoRoutes[1] !is Route.CountriesRoute
}

fun BackStack.getCurrentRoute() = last() as Route