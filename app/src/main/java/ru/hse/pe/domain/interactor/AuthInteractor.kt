package ru.hse.pe.domain.interactor

import ru.hse.pe.data.api.ApiService
import ru.hse.pe.domain.model.UserEntity
import javax.inject.Inject

class AuthInteractor @Inject constructor(private val apiService: ApiService) {
    fun addUser(user: UserEntity) = apiService.addUser(user)
}