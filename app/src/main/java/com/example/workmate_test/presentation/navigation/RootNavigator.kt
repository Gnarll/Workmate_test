package com.example.workmate_test.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.workmate_test.R
import com.example.workmate_test.presentation.screens.CountriesScreen
import com.example.workmate_test.presentation.screens.CountryDetailsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootNavigator() {
    val backStack: BackStack =
        rememberNavBackStack(Route.CountriesRoute)


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    backStack.getCurrentRoute().getTitle()?.let { title ->
                        Text(
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    if (backStack.isBackNavigationAllowed()) {
                        IconButton(onClick = { backStack.pop() }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_back),
                                contentDescription = stringResource(R.string.go_back)
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            backStack = backStack,
            onBack = {
                backStack.pop()
            },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry(key = Route.CountriesRoute) {
                    CountriesScreen(onNavigateToCountryDetails = { countryId ->
                        backStack.add(Route.CountryDetailsRoute(countryId))
                    })
                }
                entry<Route.CountryDetailsRoute> { route ->
                    CountryDetailsScreen(
                        countryId = route.countryId
                    )
                }
            }
        )
    }
}