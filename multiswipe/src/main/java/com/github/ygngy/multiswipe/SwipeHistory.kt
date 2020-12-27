/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegi"
 * Licensed under the MIT license
 *
 * Email: ygnegy@gmail.com
 * Github: https://github.com/ygngy
 */

package com.github.ygngy.multiswipe

/**
 * Base class to save last used swipe's id.
 */
sealed class SwipeHistory {
    /**
     * To change active swipe, set its id.
     * setting null value has no effect.
     */
    var id: String? = null
        set(value) {
            if (value != null) field = value
        }

    /**
     * History from Opposite (facing) side of current side.
     */
    abstract val opposite: SwipeHistory
}

/**
 * Object used to save last used swipe id in left side of row.
 */
object LeftSwipeHistory: SwipeHistory() {
    override val opposite: SwipeHistory
        get() = RightSwipeHistory
}

/**
 * Object used to save last used swipe id in right side of row.
 */
object RightSwipeHistory: SwipeHistory() {
    override val opposite: SwipeHistory
        get() = LeftSwipeHistory
}