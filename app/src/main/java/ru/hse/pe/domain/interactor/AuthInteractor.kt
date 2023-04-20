package ru.hse.pe.domain.interactor

import ru.hse.pe.data.api.ApiService
import ru.hse.pe.domain.model.UserEntity
import javax.inject.Inject

/**
 * Interactor to auth user from ViewModels
 *
 * @param apiService service for auth related api calls
 */
class AuthInteractor @Inject constructor(private val apiService: ApiService) {

    /**
     * Add user to remote db via server API.
     *
     * @param user user data
     *
     * @return response wrapped into Completable
     */
    fun addUser(user: UserEntity) = apiService.addUser(user)

    /**
     * Obtain user data from server with uid
     *
     * @param uid user uid
     *
     * @return response wrapped into Single
     */
    fun getUser(uid: String) = apiService.getUser(uid)
}