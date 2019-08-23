package com.cleveroad.bootstrap.kotlin_phone_input.choose_country

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleFragment
import com.cleveroad.bootstrap.kotlin_core.utils.misc.MiscellaneousUtils.Companion.getExtra
import com.cleveroad.bootstrap.kotlin_ext.hide
import com.cleveroad.bootstrap.kotlin_ext.show
import com.cleveroad.bootstrap.kotlin_phone_input.BuildConfig
import com.cleveroad.bootstrap.kotlin_phone_input.Constants
import com.cleveroad.bootstrap.kotlin_phone_input.R
import com.cleveroad.bootstrap.kotlin_phone_input.data.models.CountryAsset
import kotlinx.android.synthetic.main.choose_country_fragment.*

class ChooseCountryFragment : BaseLifecycleFragment<ChooseCountryViewModel>(), CountriesAdapterCallback {
    companion object {
        private val COUNTRY_EXTRA = getExtra("COUNTRY_EXTRA", ChooseCountryFragment::class.java)
        val CHOOSE_COUNTRY_EXTRA = getExtra("CHOOSE_COUNTRY_EXTRA", ChooseCountryFragment::class.java)

        fun newInstance(targetFragment: Fragment, requestCode: Int, countryCode: String? = null) =
                ChooseCountryFragment().apply {
                    setTargetFragment(targetFragment, requestCode)
                    arguments = Bundle().apply {
                        countryCode?.let { putString(COUNTRY_EXTRA, it) }
                    }
                }
    }

    override val viewModelClass = ChooseCountryViewModel::class.java

    override fun getScreenTitle() = R.string.country

    override fun hasToolbar() = true

    override fun getToolbarId() = R.id.toolbar

    override val layoutId = R.layout.choose_country_fragment

    override var endpoint = Constants.EMPTY_STRING

    override var versionName = Constants.EMPTY_STRING

    override fun getVersionsLayoutId() = View.NO_ID

    override fun getEndPointTextViewId() = View.NO_ID

    override fun getVersionsTextViewId() = View.NO_ID

    override fun isDebug() = BuildConfig.DEBUG

    override fun showBlockBackAlert() {
        // override this method if you need to show a warning when going to action back
    }

    private var countriesAdapter: CountriesAdapter? = null

    private val countriesObserver = Observer<List<CountryAsset>> {
        it?.takeIf { it.isNotEmpty() }?.let { countries ->
            rvCountries.show()
            tvNoResults.hide()
            countriesAdapter?.updateAllNotify(countries)
        } ?: let {
            rvCountries.hide()
            tvNoResults.show()
        }
    }

    override fun observeLiveData() {
        with(viewModel) {
            countriesLD.observe(this@ChooseCountryFragment, countriesObserver)
            searchCountriesLD.observe(this@ChooseCountryFragment, countriesObserver)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity?.let {
            countriesAdapter = CountriesAdapter(it, listOf(), arguments?.getString(COUNTRY_EXTRA), this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvCountries.initRecyclerView(countriesAdapter, isShowDivider = true)
        viewModel.getCountriesFromAssets()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_country_search, menu)
        onSearch(menu.findItem(R.id.itSearch))
    }

    private fun onSearch(item: MenuItem) {
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String) = false

            override fun onQueryTextChange(s: String): Boolean {
                populateAdapter(s)
                return false
            }
        })
    }

    private fun populateAdapter(query: String) {
        viewModel.searchCountries(query)
    }

    override fun onItemClick(country: CountryAsset) {
        setResult(country)
    }

    private fun setResult(country: CountryAsset) {
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, Intent().apply {
            putExtra(CHOOSE_COUNTRY_EXTRA, country)
        })
        backPressed()
    }
}