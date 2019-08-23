# Kotlin Core [![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome) <img src="https://www.cleveroad.com/public/comercial/label-android.svg" height="19"> <a href="https://www.cleveroad.com/?utm_source=github&utm_medium=label&utm_campaign=contacts"><img src="https://www.cleveroad.com/public/comercial/label-cleveroad.svg" height="19"></a>

## Meet Kotlin Core by Cleveroad

The library contains a set of base classes for Android applications.

### Description ###

Main directories of the library.

- adapters
- network
- receivers
- ui
- utils

#### Adapters ####

- BaseArrayAdapter - Simple ArrayAdapter that propose methods to add collections on pre-Honeycomb devices.
- BaseFilter - Base filter that can be easily integrated with BaseArrayAdapter. For iterating through adapter's data use getNonFilteredCount and getNonFilteredItem.
- BaseRecyclerViewAdapter - Base adapter for recycler view.

#### UI ####

- BaseLifecycleViewModel - The is a class that is responsible for preparing and managing the data for an Activity or a Fragment.
- BaseLifecycleActivity - Base activity that implements work with the ViewModel and the life cycle.
- BaseLifecycleFragment - The is a class that implements work with the ViewModel and the life cycle.
- BaseLifecycleDialogFragment - Base fragment for the implementation of the dialog with custom view.
- BaseListFragment - Base fragment for the implementation of the screen with a list of data.
- BaseDialogFragment - Base fragment for the implementation of the dialog with custom view.
- BaseFragmentPagerAdapter - Base fragment for the implementation of the ViewPager.
- BaseRecyclerViewAdapter - Base adapter for recycler view.

#### Utils ####

- MiscellaneousUtils - Get String with name for EXTRA parameter. Pattern packageName + class.simpleName + EXTRA + extraName.
- ImageUtils - Provides common methods of the utility for working with image.
- RealPathUtils - Provides common methods of the utility for working with file.
- RXUtils -  Helper class for RxJava. This class provides methods for switching between threads.

## Setup and usage
### Installation
by gradle : 
```groovy
dependencies {
    implementation 'com.cleveroad.bootstrap:kotlin-core:2.0.0'
}
```

### Usage ###
```groovy

 class SampleVM(application: Application) : BaseLifecycleViewModel(application) {
    val loadDataVM = MutableLiveData<Data>()

    fun loadData() {
        DataProvider.loadData()
            .doAsync(loadDataVM)
    }
 }

 class SampleFragment : BaseLifecycleFragment<SampleVM>() {

     override val viewModelClass = SampleVM::class.java

     override val layoutId = R.layout.fragment_sample

     override fun getScreenTitle() = NO_TITLE

     override fun hasToolbar() = false

     override fun getToolbarId() = NO_TOOLBAR

     override fun observeLiveData() {
         viewModel.run {
             loadDataVM.observe(this@SampleFragment, Observer { ... })
         }
     }
 }

  class SampleAuthVM(application: Application) : BaseLifecycleViewModel(application)

  class SampleAuthActivity : BaseLifecycleActivity<SampleAuthVM>() {

      override val viewModelClass = SampleAuthVM::class.java

      override val containerId = R.id.flContainer

      override val layoutId = R.layout.activity_sample_auth

      override fun getProgressBarId() = R.id.progressBar

      override fun getSnackBarDuration() = Snackbar.LENGTH_SHORT

      override fun observeLiveData() = Unit

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          replaceFragment(SampleFragment.newInstance())
      }
  }
```

### Support ###
If you have any questions, issues or propositions, please create a <a href="../../issues/new">new issue</a> in this repository.

If you want to hire us, send an email to sales@cleveroad.com or fill the form on <a href="https://www.cleveroad.com/contact">contact page</a>

Follow us:

[![Awesome](/images/social/facebook.png)](https://www.facebook.com/cleveroadinc/)   [![Awesome](/images/social/twitter.png)](https://twitter.com/cleveroadinc)   [![Awesome](/images/social/google.png)](https://plus.google.com/+CleveroadInc)   [![Awesome](/images/social/linkedin.png)](https://www.linkedin.com/company/cleveroad-inc-)   [![Awesome](/images/social/youtube.png)](https://www.youtube.com/channel/UCFNHnq1sEtLiy0YCRHG2Vaw)
<br/>

### License ###
* * *
    The MIT License (MIT)
    
    Copyright (c) 2016 Cleveroad Inc.
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
