package com.cleveroad.progress_upload_example.models.converters

import io.reactivex.rxjava3.annotations.Nullable

/**
 * Base implementation of [Converter]
 *
 * @param <IN>  Input type
 * @param <OUT> Output type
 *
 */

abstract class BaseConverter<IN : Any, OUT : Any> : Converter<IN, OUT> {

    override val single: SingleConverter<IN, OUT> by lazy { SingleConverter(this) }

    /**
     * Convert IN to OUT
     *
     * @param inObject object ot inToOut
     * @return Nullable [OUT] converted object
     */
    override fun inToOut(@Nullable inObject: IN?): OUT? = processConvertInToOut(inObject)

    /**
     * Convert OUT to IN
     *
     * @param outObject [OUT] object ot outToIn
     * @return Nullable [IN] Converted object
     */
    @Nullable
    override fun outToIn(@Nullable outObject: OUT?): IN? = processConvertOutToIn(outObject)

    /**
     * Convert List of IN to List of OUT
     *
     * @param inObjects [List] of [IN] objects to listInToOut
     * @return [List] of converted objects
     */
    @Nullable
    override fun listInToOut(@Nullable inObjects: List<IN>?): List<OUT> = inObjects?.mapNotNull { inToOut(it) }
            ?: listOf()

    /**
     * Convert List of OUT to List of IN
     *
     * @param outObjects [List] of [OUT] objects to listOutToIn
     * @return [List] of converted objects
     */
    @Nullable
    override fun listOutToIn(@Nullable outObjects: List<OUT>?): List<IN> = outObjects?.mapNotNull { outToIn(it) }
            ?: listOf()

    protected abstract fun processConvertInToOut(@Nullable inObject: IN?): OUT?

    protected abstract fun processConvertOutToIn(@Nullable outObject: OUT?): IN?
}
