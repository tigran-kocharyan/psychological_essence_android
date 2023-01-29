package ru.hse.pe.presentation.test

import Results
import Test
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.hse.pe.presentation.test.utils.sealed.Routes


@Composable
fun Navigation(context: TestFragment) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Test.route) {
        composable(Routes.Test.route) {
            Test(navController, context)
        }

        composable(Routes.Results.route) {
            Results()
        }
    }
}


