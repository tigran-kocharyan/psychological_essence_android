package ru.hse.pe.presentation.content.type.test.ui.compose

import Results
import Test
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.hse.pe.SharedViewModel
import ru.hse.pe.presentation.content.type.test.utils.sealed.Routes
import ru.hse.pe.presentation.content.viewmodel.ContentViewModel


@Composable
fun Navigation(
    sharedViewModel: SharedViewModel,
    viewModel: ContentViewModel,
    viewLifecycleOwner: LifecycleOwner
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Test.route) {
        composable(Routes.Test.route) {
            Test(navController, sharedViewModel)
        }

        composable(Routes.Results.route) {
            Results(viewModel, viewLifecycleOwner)
        }
    }
}


