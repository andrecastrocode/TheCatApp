package ac.andrecastro.thecatapp.presentation.viewmodel

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface SchedulerProvider {
    fun io() = Schedulers.io()
    fun main(): Scheduler = AndroidSchedulers.mainThread()
}

class DefaultSchedulerProvider : SchedulerProvider