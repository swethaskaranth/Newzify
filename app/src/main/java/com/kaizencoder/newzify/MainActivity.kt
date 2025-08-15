package com.kaizencoder.newzify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kaizencoder.newzify.presentation.headlines.HeadlinesScreen
import com.kaizencoder.newzify.presentation.navigation.Route
import com.kaizencoder.newzify.presentation.savedArticles.SavedArticlesScreen
import com.kaizencoder.newzify.ui.theme.NewzifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewzifyTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(snackBarHostState)
                    },
                    bottomBar = {
                        BottomNavigationBar(navController)
                    }
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = Route.HomeScreen
                    ){
                        composable<Route.HomeScreen> {
                            HeadlinesScreen(
                                snackBarHostState = snackBarHostState,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        composable<Route.SavedArticlesScreen> {
                            SavedArticlesScreen(
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        NavigationBarItem(
            selected = true,
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            onClick = {
                navController.navigate(Route.HomeScreen)
            }
        )

        NavigationBarItem(
            selected = true,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.icon_save),
                    contentDescription = "Saved Articles",
                    modifier = Modifier.size(24.dp)
                )
            },
            onClick = {
                navController.navigate(Route.SavedArticlesScreen)
            }
        )

    }
}
