package com.cleveroad.bootstrap.kotlin_core.ui.view_pager_adapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Base implementation of the StatePagerAdapter.
 */
class BaseFragmentStatePagerAdapter(private val fm: FragmentManager,
                                    private val fragmentInfoContainers: List<FragmentInfoContainer>)
    : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    /**
     * @return the [Fragment] array from adapter.
     */
    var fragments = SparseArray<Fragment>()
        private set

    override fun getItem(position: Int): Fragment {
        val fragmentInfoContainer = fragmentInfoContainers[position]
        return fm.fragmentFactory
                .instantiate(ClassLoader.getSystemClassLoader(), fragmentInfoContainer.fragmentClass.name)
                .apply { arguments = fragmentInfoContainer.args }
    }

    override fun instantiateItem(container: ViewGroup,
                                 position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        fragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup,
                             position: Int,
                             any: Any) {
        fragments.remove(position)
        super.destroyItem(container, position, any)
    }

    /**
     * @return page title.
     */
    override fun getPageTitle(position: Int) = fragmentInfoContainers[position].title

    /**
     * @return item count of this adapter.
     */
    override fun getCount() = fragmentInfoContainers.size
}
