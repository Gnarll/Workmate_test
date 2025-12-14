package com.example.workmate_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.workmate_test.data.repositories.CountryRepositoryImpl
import com.example.workmate_test.presentation.navigation.RootNavigator
import com.example.workmate_test.presentation.ui.theme.Workmate_testTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var repo: CountryRepositoryImpl
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Workmate_testTheme {
                RootNavigator()
            }
        }
    }
}