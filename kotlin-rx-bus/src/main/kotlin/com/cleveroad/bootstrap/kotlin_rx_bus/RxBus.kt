package com.cleveroad.bootstrap.kotlin_rx_bus

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.PublishProcessor

object RxBus {
    private val bus = PublishProcessor.create<Any>()

    /**
     * Send new event
     *
     * @param obj [Any]
     */
    fun send(obj: Any) {
        bus.onNext(obj)
    }

    /**
     * Filter events in RxBus by [Class]
     *
     * @param eventClass [Class]
     *
     * @return [Flowable]
     */
    fun <T> filter(eventClass: Class<T>): Flowable<T> = toFlowable()
            .filter { event -> event.javaClass == eventClass }
            .cast(eventClass)

    private fun toFlowable() = bus
}