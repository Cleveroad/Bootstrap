package com.cleveroad.bootstrap.kotlin_phone_input.data.converter

import io.reactivex.rxjava3.core.FlowableTransformer
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.SingleTransformer
import org.json.JSONArray

/**
 * Encapsulate logic for converting from one type to another and vice versa

 * @param <IN>  Input type
 * *
 * @param <OUT> Output type
</OUT></IN> */
internal interface Converter<IN, OUT> {

    fun convertInToOut(inObject: IN?): OUT?

    fun convertOutToIn(outObject: OUT?): IN?

    fun convertListInToOut(inObjects: List<IN?>?): List<OUT>?

    fun convertListOutToIn(outObjects: List<OUT?>?): List<IN>?

    fun singleINtoOUT(): ObservableTransformer<IN?, OUT?>

    fun singleOUTtoIN(): ObservableTransformer<OUT?, IN?>

    fun listINtoOUT(): ObservableTransformer<List<IN?>?, List<OUT?>?>

    fun listOUTtoIN(): ObservableTransformer<List<OUT?>?, List<IN?>?>

    fun singleFlowINtoOUT(): FlowableTransformer<IN?, OUT?>

    fun singleFlowOUTtoIN(): FlowableTransformer<OUT?, IN?>

    fun listFlowINtoOUT(): FlowableTransformer<List<IN?>?, List<OUT?>?>

    fun listFlowOUTtoIN(): FlowableTransformer<List<OUT?>?, List<IN?>?>

    fun singleINtoOUTSingle(): SingleTransformer<OUT, IN>

    fun singleOUTtoINSingle(): SingleTransformer<IN?, OUT>

    fun listINtoOUTSingle(): SingleTransformer<List<OUT>, List<IN>>

    fun listOUTtoINSingle(): SingleTransformer<List<IN>, List<OUT>>

    fun jsonArraySingleINtoOUT(): SingleTransformer<JSONArray?, List<OUT>>
}