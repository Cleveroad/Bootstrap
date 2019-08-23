package com.cleveroad.bootstrap.kotlin_core.adapters

import android.content.Context
import android.database.DataSetObserver
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cleveroad.bootstrap.kotlin_core.R
import com.cleveroad.bootstrap.kotlin_core.utils.misc.assertInstanceOf
import com.cleveroad.bootstrap.kotlin_core.utils.misc.assertNotEquals

/**
 * Base filter that can be easily integrated with [BaseArrayAdapter].<br></br><br></br>
 * For iterating through adapter's data use [.getNonFilteredCount] and [.getNonFilteredItem].
 */
abstract class BaseFilter<T>(highlightColor: Int) : Filter() {

    companion object {
        private const val FILTERED_EMPTY_LIST_COUNT = -1
        private const val EMPTY_LIST_COUNT = 0
        private const val DEFAULT_COLOR = 0
        private const val INDEX_OF_NOT_FOUND_VALUE = -1
        private const val INDEX_STEP = 1
    }

    private var adapter: FilterableAdapter<T>? = null
    private var lastConstraint: CharSequence? = null
    private var lastResults: FilterResults? = null
    var dataSetObserver: DataSetObserver? = null
        private set
    var adapterDataObserver: RecyclerView.AdapterDataObserver? = null
        private set

    var highlightColor: Int = DEFAULT_COLOR
        @Throws(AssertionError::class)
        set(value) {
            value.assertNotEquals(DEFAULT_COLOR, "HighlightColor")
            field = value
        }

    init {
        this.highlightColor = highlightColor
    }

    constructor(context: Context) : this(ContextCompat.getColor(context, R.color.theme_color_accent))

    fun isFiltered() = lastResults?.count?.let { it > FILTERED_EMPTY_LIST_COUNT } ?: false

    fun getCount() = lastResults?.count ?: EMPTY_LIST_COUNT

    @Throws(AssertionError::class)
    internal fun init(adapter: FilterableAdapter<T>) {
        this.adapter = adapter
        dataSetObserver = object : DataSetObserver() {

            override fun onInvalidated() {
                super.onInvalidated()
                if (!isFiltered()) {
                    return
                }
                lastResults = FilterResults().apply {
                    count = FILTERED_EMPTY_LIST_COUNT
                    values = emptyList<Any>()
                }
            }
        }
        adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                if (!isFiltered()) {
                    return
                }
                performFiltering(lastConstraint)
            }
        }
    }

    protected fun getNonFilteredCount() = adapter?.getNonFilteredCount() ?: EMPTY_LIST_COUNT

    protected fun getNonFilteredItem(position: Int) = adapter?.getNonFilteredItem(position)

    override fun performFiltering(constraint: CharSequence?) = performFilteringImpl(constraint)

    /**
     * Perform filtering as always. Returned [FilterResults] object must be non-null.
     * @param constraint the constraint used to filter the data
     * @return filtering results. <br></br>
     * You can set [FilterResults.count] to -1 to specify that no filtering was applied.<br></br>
     * [FilterResults.values] must be instance of [List].
     */
    protected abstract fun performFilteringImpl(constraint: CharSequence?): FilterResults

    @Throws(AssertionError::class)
    override fun publishResults(constraint: CharSequence, results: FilterResults) {
        if (results.count > EMPTY_LIST_COUNT) {
            results.values.assertInstanceOf<List<*>>("Values")
        } else {
            return
        }
        lastConstraint = constraint
        lastResults = results
        adapter?.notifyDataSetChanged()
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(ArrayIndexOutOfBoundsException::class)
    fun getItem(position: Int) = (lastResults?.values as List<T>)[position]

    fun highlightFilteredSubstring(name: String): Spannable {
        with(SpannableString(name)) {
            if (!isFiltered()) return this
            val filteredString = lastConstraint.toString().trim().toLowerCase()
            val lowercase = name.toLowerCase()
            val length = filteredString.length
            var index = INDEX_OF_NOT_FOUND_VALUE
            var prevIndex: Int
            do {
                prevIndex = index
                index = lowercase.indexOf(filteredString, prevIndex + INDEX_STEP)
                if (index == INDEX_OF_NOT_FOUND_VALUE) break
                setSpan(ForegroundColorSpan(highlightColor), index, index + length, 0)
            } while (true)
            return this
        }
    }

    internal interface FilterableAdapter<T> {
        fun getNonFilteredCount(): Int
        fun getNonFilteredItem(position: Int): T
        fun notifyDataSetChanged()
        fun withFilter(filter: BaseFilter<T>?)
        fun highlightFilteredSubstring(text: String): Spannable
    }
}