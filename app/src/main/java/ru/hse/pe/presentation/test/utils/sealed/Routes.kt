package ru.hse.pe.presentation.test.utils.sealed

// It contains route names to all three screens
sealed class Routes(val route: String) {
    object Test : Routes("test")
    object Results : Routes("results")
}
