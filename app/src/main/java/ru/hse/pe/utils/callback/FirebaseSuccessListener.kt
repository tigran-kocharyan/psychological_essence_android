package ru.hse.pe.utils.callback

/**
 * Интерфейс для обработки события успешной подкачки данных firebase
 */
interface FirebaseSuccessListener {
    fun onDataFound(isDataFetched: Boolean)
}