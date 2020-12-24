/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegy"
 * Licensed under the MIT license
 *
 * Email: ygnegy@gmail.com
 * Github: https://github.com/ygngy
 */

package com.github.ygngy.multiswipe

/**
 * Used to notify direction of Swipe
 *
 * [START] swipe is from view's layout start side
 *
 * [END] swipe is from view's layout end side
 *
 * [NONE] there is no swipe
 */
enum class SwipeDirection {
    /**
     * [START] swipe is from view's layout start side
     */
    START,

    /**
     * [END] swipe is from view's layout end side
     */
    END,

    /**
     * [NONE] there is no swipe
     */
    NONE
}

interface MultiSwipeListener {

    /**
     * This method could be used to clear on screen widgets such as FABs
     * while swiping
     * This method may be called a lot in each swipe cause of [direction] change.
     * [direction] is row's layout side of swipe's edge or [SwipeDirection.NONE] if no swipe is active.
     *
     * [swipeListSize] is number of swipes in current direction
     */
    fun swiping(direction: SwipeDirection, swipeListSize: Int)

    /**
     * This method will be called when user swipes enough to triggers swipe's action.
     * [swipeId] is the id of the swiped [Swipe]
     * [data] is data returned from [MultiSwipe.onSwipeDone] (from ViewHolder.onSwipeDone)
     */
    fun onSwipeDone(swipeId: String, data: Any?)

}