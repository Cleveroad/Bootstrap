package com.cleveroad.bootstrap.kotlin_phone_input.choose_country

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cleveroad.bootstrap.kotlin_core.ui.adapter.BaseRecyclerViewAdapter
import com.cleveroad.bootstrap.kotlin_ext.clickWithDebounce
import com.cleveroad.bootstrap.kotlin_phone_input.Constants.PHONE_PREF
import com.cleveroad.bootstrap.kotlin_phone_input.R
import com.cleveroad.bootstrap.kotlin_phone_input.data.models.CountryAsset
import com.cleveroad.bootstrap.kotlin_phone_input.utils.CountryFlag.getCountryFlagResIdByCode
import java.lang.ref.WeakReference

interface CountriesAdapterCallback {
    fun onItemClick(country: CountryAsset)
}

interface AdapterCallback {
    fun onItemClick(itemPosition: Int)
}

class CountriesAdapter(context: Context,
                       data: List<CountryAsset>,
                       private val currentCountryCode: String?,
                       countriesAdapterCallback: CountriesAdapterCallback) :
        BaseRecyclerViewAdapter<CountryAsset, CountryViewHolder>(context, data),
        AdapterCallback {

    private val weakRefCallback = WeakReference(countriesAdapterCallback)

    override fun onItemClick(itemPosition: Int) {
        weakRefCallback.get()?.onItemClick(getItem(itemPosition))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            CountryViewHolder.newInstance(LayoutInflater.from(parent.context), parent, this)


    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(getItem(position), currentCountryCode)
    }
}

class CountryViewHolder(itemView: View, private val adapterCallback: AdapterCallback) : RecyclerView.ViewHolder(itemView) {
    companion object {
        internal fun newInstance(inflater: LayoutInflater, parent: ViewGroup?, adapterCallback: AdapterCallback) =
                CountryViewHolder(inflater.inflate(R.layout.country_item, parent, false), adapterCallback)
    }

    private val tvCountryName = itemView.findViewById<TextView>(R.id.tvCountryName)
    private val tvCountryCode = itemView.findViewById<TextView>(R.id.tvCountryCode)
    private val ivCountryIcon = itemView.findViewById<ImageView>(R.id.ivCountryIcon)

    fun bind(country: CountryAsset, currentCountryCode: String?) {
        itemView.clickWithDebounce { adapterCallback.onItemClick(adapterPosition) }
        with(country) {
            tvCountryName.text = name
            ivCountryIcon.setImageResource(getCountryFlagResIdByCode(itemView.context, ab))
            tvCountryCode.apply {
                text = PHONE_PREF
                append(dialCode.toString())
                setCompoundDrawablesRelativeWithIntrinsicBounds(getCheckDrawable(currentCountryCode, ab), 0, 0, 0)
            }
        }
    }

    private fun getCheckDrawable(currentCountryCode: String?, itemCountryCode: String): Int =
            if (currentCountryCode?.equals(itemCountryCode, true) == true) R.drawable.ic_check_green_highlight_24dp else 0
}