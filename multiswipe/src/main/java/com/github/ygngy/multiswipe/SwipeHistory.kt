/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegy"
 * Licensed under the MIT license
 *
 * Email: ygnegy@gmail.com
 * Github: https://github.com/ygngy
 */

package com.github.ygngy.multiswipe

/**
 * Base class to save last used swipe id
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
     * History from Opposite (facing) side of current side
     */
    abstract val opposite: SwipeHistory
}

/**
 * Object used to save last used swipe id in left side of list row
 */
object LeftSwipeHistory: SwipeHistory() {
    override val opposite: SwipeHistory
        get() = RightSwipeHistory
}

/**
 * Object used to save last used swipe id in right side of list row
 */
object RightSwipeHistory: SwipeHistory() {
    override val opposite: SwipeHistory
        get() = LeftSwipeHistory
}