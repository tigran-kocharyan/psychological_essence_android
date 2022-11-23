package ru.hse.pe.presentation.test.utils.sealed

import ru.hse.pe.domain.model.Test

sealed class DataState {
    class Success(val data: Test?) : DataState()
    class Failure(val message: String) : DataState()
    object Loading : DataState()
    object Empty : DataState()
}