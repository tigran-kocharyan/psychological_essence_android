package ru.hse.pe.presentation.test.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.hse.pe.presentation.test.utils.sealed.Routes

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Test.route) {
        composable(Routes.Test.route) {
            Test(navController)
        }

        composable(Routes.Results.route) {
            //  Results(navController)
        }
    }
}

@Composable
fun Test(navController: NavController) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text(text = "Test", modifier = Modifier.padding(top = 5.dp))

            Button(onClick = {
                navController.navigate(Routes.Results.route)
            }) {
                Text(text = "Navigate to Results", color = Color.White)
            }

        }
    }
}



