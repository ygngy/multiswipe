/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegi"
 * Licensed under the MIT license
 *
 * Email: ygnegy@gmail.com
 * Github: https://github.com/ygngy
 */

package com.github.ygngy.multiswipe

/**
 * Used to define the layout side of the row where swipe was started from.
 *
 * [START] Swipe is started from view's start side.
 *
 * [END] Swipe is started from view's end side.
 *
 * [NONE] Swipe is closing or there is no swipe.
 */
enum class SwipeDirection {
    /**
     * Swipe is started from view's start side.
     */
    START,

    /**
     * Swipe is started from view's end side.
     */
    END,

    /**
     * Swipe is closing or there is no swipe.
     */
    NONE
}

/**
 * Used to listen to swipe events.
 */
interface MultiSwipeListener {

    /**
     * This method could be used to clear on screen widgets such as FABs while swiping.
     * This method may be called a lot in each swipe as a result of [direction] change.
     *
     * @param direction Layout side ([START][SwipeDirection.START]/[END][SwipeDirection.END])
     * of the row where swipe was started from or [NONE][SwipeDirection.NONE]
     * if swipe is closing or no swipe is active.
     * @param swipeListSize Number of available swipes in current direction.
     */
    fun swiping(direction: SwipeDirection, swipeListSize: Int)

    /**
     * This method will be called after [MultiSwipe.onSwipeDone]
     * when swipe is done and [swipeId] action needs to be done.
     *
     * @param swipeId The id of the [Swipe].
     * @param data Data returned from [MultiSwipe.onSwipeDone] (from ViewHolder.onSwipeDone).
     */
    fun onSwipeDone(swipeId: Int, data: Any?)

}