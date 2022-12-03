package ru.hse.pe.presentation.test

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.test.viewModels.TestViewModel
import ru.hse.pe.presentation.test.utils.sealed.Routes


@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Test.route) {
        composable(Routes.Test.route) {
            TestActivity().Test(navController)
        }

        composable(Routes.Results.route) {
            TestActivity().Results()
        }
    }
}


