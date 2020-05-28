package com.cleveroad.bootstrap.kotlin_core.ui.view_pager_adapter

import android.os.Bundle
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleFragment


/**
 * The data class for creating the PagerAdapter element.
 */
data class FragmentInfoContainer(val fragmentClass: Class<out BaseLifecycleFragment<*>>,
                                 val title: String = "",
                                 val args: Bundle = Bundle.EMPTY)