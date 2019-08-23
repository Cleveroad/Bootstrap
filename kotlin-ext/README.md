# Kotlin Ext [![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome) <img src="https://www.cleveroad.com/public/comercial/label-android.svg" height="19"> <a href="https://www.cleveroad.com/?utm_source=github&utm_medium=label&utm_campaign=contacts"><img src="https://www.cleveroad.com/public/comercial/label-cleveroad.svg" height="19"></a>

## Meet Kotlin Ext by Cleveroad

Collection of most used Kotlin extensions.

### Description ###

List of Most commonly used Kotlin Extensions

- Activity
- Assertion
- Bitmap
- Common
- Context
- EditText
- File
- ImageView
- Keyboard
- Screen (refer to Context)
- String
- TextView
- Uri
- View

## Setup and usage
### Installation
by gradle : 
```groovy
dependencies {
    implementation 'com.cleveroad.bootstrap:kotlin-ext:2.0.0'
}
```
### Usage ###
#### Activity ####
```groovy
fun Activity.setAdjustNothing(): Unit //To set windowSoftInputMode SOFT_INPUT_ADJUST_NOTHING at runtime
fun Activity.setAdjustResize(): Unit //To set windowSoftInputMode SOFT_INPUT_ADJUST_RESIZE at runtime
fun Activity.setAdjustPan(): Unit //To set windowSoftInputMode SOFT_INPUT_ADJUST_PAN at runtime
fun Activity.setFullScreen(): Unit //To hide all screen decorations (such as the status bar) while this window is displayed
fun Activity.setRotatable(): Unit //To set requestedOrientation SCREEN_ORIENTATION_SENSOR at runtime
fun Activity.setOnlyPortraitMode(): Unit //To set requestedOrientation SCREEN_ORIENTATION_PORTRAIT at runtime
fun Activity.setOnlyLandscapeMode(): Unit //To set requestedOrientation SCREEN_ORIENTATION_LANDSCAPE at runtime
```
#### Assertion ####
```groovy
fun Any?.assertNotNull(parameterName: String = ""): Any //throw AssertionError if parameter is null
fun Boolean.assertTrue(message: String): Boolean //throw AssertionError if boolean value is not true
fun Boolean.assertFalse(message: String): Boolean //throw AssertionError if boolean value is not false
fun Any?.assertNotEquals(anotherValue: Any, parameterName: String = "parameter"): Boolean //throw AssertionError if value is not equals another value
fun String?.assertNotEmpty(parameterName: String): Boolean //throw AssertionError if string is null or empty
fun <reified T> Any?.assertInstanceOf(parameterName: String): Boolean //throw AssertionError is value is not belong to class T
```
#### Bitmap ####
```groovy
fun Bitmap?.getIfNotRecycled(): Bitmap? //return bitmap if it is recycled or null
fun Bitmap?.checkAndRecycle(): Unit? //check if not null and recycle if not recycled
```
#### Common ####
```groovy
fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R?
fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3) -> R?): R?
fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, block: (T1, T2, T3, T4) -> R?): R?
fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, R : Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, p5: T5?, block: (T1, T2, T3, T4, T5) -> R?): R?
fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any, R : Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, p5: T5?, p6: T6?, block: (T1, T2, T3, T4, T5, T6) -> R?): R?
//Calls the specified function [block] with `p1, p2...p6` not null values as its arguments and returns its result

inline fun <T, R> withNotNull(receiver: T?, block: T.() -> R): R? //Calls the specified function [block] with not null `this` value as its receiver and returns its result
```
#### Context ####
```groovy
fun Context.getFontResourcesCompat(@FontRes fontRes: Int): Typeface? //Returns a font Typeface associated with a particular resource ID or throws NotFoundException if the given ID does not exist
fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable? //Returns a drawable object associated with a particular resource ID or throws NotFoundException if the given ID does not exist
fun Context.getColorCompat(@ColorRes id: Int): Int //Returns a color object associated with a particular resource ID or throws NotFoundException if the given ID does not exist
fun Context.getInteger(@IntegerRes intRes: Int): Int //Returns a integer object associated with a particular resource ID or throws NotFoundException if the given ID does not exist
fun Context.getStringArray(@ArrayRes id: Int): Array<String>//Returns the string array object associated with a particular resource ID or throws NotFoundException if the given ID does not exist
fun Context.isNetworkConnected(): Boolean //return true if network connectivity exists, false otherwise.
```
#### EditText ####
```groovy
fun EditText.getStringText(): String //Return the text the EditText is displaying as String.
fun EditText.getTrimStringText(): String //Return the text the EditText is displaying as String and trailing whitespace removed.
fun EditText.setOnDrawableClick(type: DrawablePositionTypes, callback: () -> Unit): Unit //handle touch on compoundDrawables
```
#### File ####
```groovy
fun File.getUri(context: Context, authority: String): Uri //return a content URI for the file
```
#### ImageView ####
```groovy
fun ImageView.getBitmap(): Bitmap? //Returns the bitmap used by view drawable to render
```
#### Keyboard ####
```groovy
fun Context.showKeyboard() //Extension method to provide show keyboard
fun androidx.fragment.app.Fragment.showKeyboard() //Extension method to provide show keyboard for Activity androidx
fun Activity.hideKeyboard() //Extension method to provide hide keyboard for Activity
fun SupportFragment.hideKeyboard() //Extension method to provide hide keyboard for SupportFragment
fun Fragment.hideKeyboard() //Extension method to provide hide keyboard for Fragment
```
#### Screen ####
```groovy
fun Context.dpToPx(dp: Float): Float //return Float calculated density independent pixels (DiP, DP) to device pixels
fun Context.pxToDp(px: Float): Float //return Float calculated device pixels as density independent pixels (DiP, DP)
fun Context.getScreenSize(): Point //Returns size of screen as Point
fun Context.getScreenDisplayMetrics(): DisplayMetrics // Returns size of screen as DisplayMetrics
```
#### String ####
```groovy
fun String.toUri(): Uri //Creates a Uri which parses the given encoded URI string
fun String.containsDigit(): Boolean //Returns true if contains a digit.
fun String.containsSymbolOrNumber(): Boolean //Returns true if string matches the "(?=.*?[^a-zA-Z\\s]).+\$" regular expression.
fun String.containsUpperCase(): Boolean //Returns true if contains character in upper case
fun String.containsLowerCase(): Boolean //Returns true if contains character in lower case
fun String?.fromBase64(): ByteArray? //Decode a string from Base64 to ByteArray
fun String?.toBase64FromString(): String? //Encode a string to Base64
fun String?.fromBase64ToString(): String? //Decode a string from Base64 to String
```
#### TextView ####
```groovy
fun TextView.getStringText(): String //Return the text the TextView is displaying
fun TextView.getTrimStringText(): String //Return the trimmed text the TextView is displaying
fun TextView.addFocusChangedListener(listener: (isHasFocus: Boolean) -> Unit) //Register a callback to be invoked when focus of this view changed
fun TextView.setFont(fontId: Int) //Sets the typeface and style in which the text should be displayed
fun TextView.setTextColorCompat(colorId: Int) //Sets the text color for all the states (normal, selected, focused) to be this color.
```
#### Uri ####
```groovy
fun Uri.checkRealFilePath(): String //Check whether "contentUri" is real file path (not content uri)
```
#### View ####
```groovy
fun View.OnClickListener.setClickListeners(vararg views: View) //Register a callback to be invoked when this views is clicked
fun View.isVisible(): Boolean //Return true if view has visibility Visible
fun View.hide(gone: Boolean = true) //if gone set visibility GONE else INVISIBLE
fun View.show() //set view visibility VISIBLE
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
