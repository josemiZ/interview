package com.josemiz.interview.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShiftModel(
    val role: String,
    val name: String,
    val start_date: String,
    val end_date: String,
    val color: String
) : Parcelable

data class ShiftListModel(
    var shifts: List<ShiftModel>
)
