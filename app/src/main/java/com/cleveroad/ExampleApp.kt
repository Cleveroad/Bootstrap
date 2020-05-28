package com.cleveroad

import android.app.Application

class ExampleApp : Application() {

    companion object {
        lateinit var instance: ExampleApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        getInstances()
    }

    private fun getInstances() {
        instance = this
    }
}
