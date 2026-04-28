package com.willfp.reforges.api.extensions

fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()