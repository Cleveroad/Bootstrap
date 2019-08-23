package com.cleveroad.bootstrap.kotlin_core.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

/**
 * Simple ArrayAdapter that propose methods to add collections on pre-Honeycomb devices.
 */
abstract class BaseArrayAdapter<T>(context: Context) : ArrayAdapter<T>(context, 0),
        BaseFilter.FilterableAdapter<T> {

    protected val inflater = LayoutInflater.from(context)
    private var filter: BaseFilter<T>? = null

    override fun withFilter(filter: BaseFilter<T>?) {
        this.filter?.run {
            unregisterDataSetObserver(dataSetObserver)
        }
        this.filter = filter?.apply {
            init(this@BaseArrayAdapter)
            registerDataSetObserver(dataSetObserver)
        }
    }

    override fun getFilter() = filter ?: super.getFilter()

    /**
     * !! is used because the function isFiltered() have already checked null
     */
    override fun highlightFilteredSubstring(text: String): Spannable = filter?.highlightFilteredSubstring(text)
            ?: SpannableString(text)

    abstract override fun getView(position: Int, convertView: View?, parent: ViewGroup): View

    override fun getItem(position: Int) = filter?.getItem(position) ?: super.getItem(position)

    /**
     * !! is used because the function isFiltered have already checked null
     */
    override fun getCount() = filter?.getCount() ?: super.getCount()

    override fun getNonFilteredCount() = super.getCount()

    override fun getNonFilteredItem(position: Int) = super.getItem(position)

    /**
     * Adds the specified items at the end of the array.
     * @param collection The items to add at the end of the array.
     */
    @SuppressLint("ObsoleteSdkInt")
    override fun addAll(collection: Collection<T>) {
        if (collection.isNotEmpty()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                setNotifyOnChange(false)
                collection.forEach { add(it) }
                notifyDataSetChanged()
            } else {
                super.addAll(collection)
            }
        }
    }

    /**
     * Adds the specified items at the end of the array.
     * @param collection The items to add at the end of the array.
     */
    @SuppressLint("ObsoleteSdkInt")
    override fun addAll(vararg collection: T) {
        if (collection.isNotEmpty()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                setNotifyOnChange(false)
                collection.forEach { add(it) }
                notifyDataSetChanged()
            } else {
                super.addAll(*collection)
            }
        }
    }
}
