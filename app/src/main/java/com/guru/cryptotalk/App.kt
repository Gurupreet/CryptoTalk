package com.guru.cryptotalk

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import org.koin.android.ext.android.startKoin
import com.guru.cryptotalk.di.*

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        startKoin(this, listOf(AppModule))
    }
}