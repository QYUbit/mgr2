package com.qyub.mgr2.util

import java.time.YearMonth

fun YearMonth.toEpochRange(): LongRange {
    val start = this.atDay(1).toEpochDay()
    val end = this.atEndOfMonth().toEpochDay()
    return start..end
}