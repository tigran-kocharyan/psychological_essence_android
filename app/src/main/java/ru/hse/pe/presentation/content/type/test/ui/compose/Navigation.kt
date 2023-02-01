package ru.hse.pe.presentation.content.type.test.ui.compose

import Results
import Test
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.hse.pe.SharedViewModel
import ru.hse.pe.presentation.content.type.test.utils.sealed.Routes


@Composable
fun Navigation(sharedViewModel: SharedViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Test.route) {
        composable(Routes.Test.route) {
            Test(navController, sharedViewModel)
        }

        composable(Routes.Results.route) {
            Results()
        }
    }
}


