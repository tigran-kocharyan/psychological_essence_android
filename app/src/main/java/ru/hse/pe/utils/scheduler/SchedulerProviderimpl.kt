package ru.hse.pe.utils.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of interface [SchedulersProvider].
 * Created by Kocharyan Tigran on 04.03.2022.
 */
class SchedulersProviderImpl() : SchedulersProvider {
    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}