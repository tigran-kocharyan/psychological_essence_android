package ru.hse.pe.domain.interactor

import ru.hse.pe.data.api.ApiService
import ru.hse.pe.domain.model.UserEntity
import javax.inject.Inject

/**
 * Interactor to add user from ViewModels
 *
 * @param apiService service for auth related api calls
 */
class AuthInteractor @Inject constructor(private val apiService: ApiService) {

    /**
     * Obtain quizzes from server data store
     *
     * @param user user data
     * @return response wrapped into Completable
     */
    fun addUser(user: UserEntity) = apiService.addUser(user)
}