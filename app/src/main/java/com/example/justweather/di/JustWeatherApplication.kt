package com.example.justweather.di

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.justweather.BuildConfig
import com.example.justweather.data.workers.DeleteMarkedItemsWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class JustWeatherApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var hiltWorkerFactory: HiltWorkerFactory
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        enqueueDeleteMarkedItemsWorker()
    }

    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder()
        .setWorkerFactory(hiltWorkerFactory)
        .build()

    private fun enqueueDeleteMarkedItemsWorker() {
        val periodicWorkRequest = PeriodicWorkRequestBuilder<DeleteMarkedItemsWorker>(
            repeatInterval = 7, // repeat every week
            repeatIntervalTimeUnit = TimeUnit.DAYS
        ).build()
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                DELETE_MARKED_ITEMS_WORK_ID,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
    }

    companion object {
        private const val DELETE_MARKED_ITEMS_WORK_ID =
            "com.example.justweather.data.workers.DeleteMarkedItemsWorker"
    }
}