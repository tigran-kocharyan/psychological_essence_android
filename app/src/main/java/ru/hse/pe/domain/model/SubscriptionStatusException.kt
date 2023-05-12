package ru.hse.pe.domain.model

/**
 * Исключение отправляется в случае ошибки сервера на запрос получения статуса подписки пользователя.
 */
class SubscriptionStatusException(message: String = "") : Exception(message)