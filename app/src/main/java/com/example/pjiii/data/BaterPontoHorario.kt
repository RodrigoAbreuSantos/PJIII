package com.example.pjiii.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BaterPontoHorario(
    var dia: String? = null,
    var OutOrIn: String? = null,
    var hr: Int = 0,
    var Mins: Int = 0
): Parcelable
