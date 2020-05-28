package com.cleveroad.composite_adapter.models

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize


@Parcelize
data class NotificationHeader(@StringRes val resId: Int) : Parcelable
