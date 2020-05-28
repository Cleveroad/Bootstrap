package com.cleveroad.bootstrap.kotlin_core.app_focus

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner

/**
 * Util for detecting foreground/background status changing for an application
 */
object AppFocusLifecycleObserver : LifecycleObserver {

    private var observers: MutableSet<ForegroundObserver> = mutableSetOf()

    /**
     * Call this function in your Application onCreate, to start tracking
     */
    fun start() = ProcessLifecycleOwner.get().lifecycle.addObserver(this)

    /**
     * Call this function when you want to stop tracking foreground status changing
     */
    fun stop() = ProcessLifecycleOwner.get().lifecycle.removeObserver(this)

    /**
     * @return state is application foreground or not
     */
    fun isForeground() =
            ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onResume() = notifyChanges()


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onPause() = notifyChanges()


    /**
     * Add Observer/listener to listen foreground state changing
     */
    fun addObserver(observer: ForegroundObserver) {
        observers.add(observer)
    }

    /**
     * Remove Observer/listener of foreground state changing
     */
    fun removeObserver(observer: ForegroundObserver) {
        observers.remove(observer)
    }


    /**
     * notify all observers about foreground state changing
     */
    private fun notifyChanges() {
        val isForeground = isForeground()
        observers.forEach { it.onForegroundStateChanged(isForeground) }
    }

}

/**
 * Interface for use as an observer of foreground state changing
 */
interface ForegroundObserver {
    fun onForegroundStateChanged(isForeground: Boolean)
}