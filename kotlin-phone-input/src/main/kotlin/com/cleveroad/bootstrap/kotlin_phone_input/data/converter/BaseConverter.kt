package com.cleveroad.bootstrap.kotlin_phone_input.data.converter

import io.reactivex.FlowableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer
import org.json.JSONArray
import org.json.JSONObject

/**
 * Base implementation of [Converter]

 * @param <IN>  Input type
 * *
 * @param <OUT> Output type
</OUT></IN> */
internal abstract class BaseConverter<IN : Any, OUT : Any> : Converter<IN, OUT> {

    override fun convertInToOut(inObject: IN?) = inObject?.let { processConvertInToOut(it) }

    override fun convertOutToIn(outObject: OUT?) = outObject?.let { processConvertOutToIn(it) }

    override fun convertListInToOut(inObjects: List<IN?>?) = inObjects?.mapNotNull { convertInToOut(it) }
            ?: listOf()

    override fun convertListOutToIn(outObjects: List<OUT?>?) = outObjects?.mapNotNull { convertOutToIn(it) }
            ?: listOf()

    protected abstract fun processConvertInToOut(inObject: IN): OUT

    protected abstract fun processConvertOutToIn(outObject: OUT): IN

    override fun singleINtoOUT(): ObservableTransformer<IN?, OUT?> = ObservableTransformer { inObservable ->
        inObservable.map { convertInToOut(it) }
    }

    override fun singleOUTtoIN(): ObservableTransformer<OUT?, IN?> = ObservableTransformer { outObservable ->
        outObservable.map { convertOutToIn(it) }
    }

    override fun listINtoOUT(): ObservableTransformer<List<IN?>?, List<OUT?>?> = ObservableTransformer { inObservable ->
        inObservable.map { convertListInToOut(it) }
    }

    override fun listOUTtoIN(): ObservableTransformer<List<OUT?>?, List<IN?>?> = ObservableTransformer { outObservable ->
        outObservable.map { convertListOutToIn(it) }
    }

    override fun singleFlowINtoOUT(): FlowableTransformer<IN?, OUT?> = FlowableTransformer { inFlowable ->
        inFlowable.map { convertInToOut(it) }
    }

    override fun singleFlowOUTtoIN(): FlowableTransformer<OUT?, IN?> = FlowableTransformer { outFlowable ->
        outFlowable.map { convertOutToIn(it) }
    }

    override fun listFlowINtoOUT(): FlowableTransformer<List<IN?>?, List<OUT?>?> = FlowableTransformer { inFlowable ->
        inFlowable.map { convertListInToOut(it) }
    }

    override fun listFlowOUTtoIN(): FlowableTransformer<List<OUT?>?, List<IN?>?> = FlowableTransformer { outFlowable ->
        outFlowable.map { convertListOutToIn(it) }
    }

    override fun singleINtoOUTSingle() = SingleTransformer<OUT, IN> { it.map { convertOutToIn(it) } }

    override fun singleOUTtoINSingle() = SingleTransformer<IN?, OUT> { it.map { convertInToOut(it) } }

    override fun listINtoOUTSingle() = SingleTransformer<List<OUT>, List<IN>> {
        it.map { convertListOutToIn(it) }
    }

    override fun listOUTtoINSingle() = SingleTransformer<List<IN>, List<OUT>> {
        it.map { convertListInToOut(it) }
    }

    @Suppress("UNCHECKED_CAST")
    override fun jsonArraySingleINtoOUT() = SingleTransformer<JSONArray?, List<OUT>> {
        it.map {
            convertListInToOut(
                    mutableListOf<JSONObject>().apply {
                        for (index in (0 until it.length())) {
                            this.add(it.optJSONObject(index))
                        }
                    }.toList() as List<IN>)
        }
    }
}