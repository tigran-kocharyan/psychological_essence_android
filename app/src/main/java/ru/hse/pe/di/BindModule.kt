package ru.hse.pe.di

import dagger.Binds
import dagger.Module
import ru.hse.pe.utils.scheduler.SchedulersProvider
import ru.hse.pe.utils.scheduler.SchedulersProviderImpl

/**
 * Module with binding details of interfaces with their implementations.
 * Created by Kocharyan Tigran on 04.03.2022.
 */
@Module
interface BindModule {
    @Binds
    fun bindSchedulers(impl: SchedulersProviderImpl): SchedulersProvider
}