package com.cleveroad.bootstrap.kotlin_core.ui.adapter.multiselect

interface MultiSelectCallback<T: Selectable> {

    fun switchPickMode(isOn: Boolean)

    fun onSelected(item: T)
}