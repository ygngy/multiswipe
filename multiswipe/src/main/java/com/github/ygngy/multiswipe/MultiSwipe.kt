/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegi"
 * Licensed under the MIT license
 *
 * Email: ygnegy@gmail.com
 * Github: https://github.com/ygngy
 */

package com.github.ygngy.multiswipe

/**
 * To represent swipes in correct order for left and right layout sides
 * ViewHolder must implement this interface
 */
interface MultiSwipe {
    /**
     * **Caution**: Prevent creating objects in this method
     * because this method may be called a lot.
     *
     * @return ordered swipes. First swipe must be in first position
     * for left side of view.
     */
    val leftSwipeList: LeftSwipeList?

    /**
     * **Caution**: Prevent creating objects in this method
     * because this method may be called a lot.
     *
     * @return ordered swipes. First swipe must be in first position
     * for right side of view.
     */
    val rightSwipeList: RightSwipeList?



    /**
     * This method will be called when user swipes enough to triggers swipe's action.
     * [swipeId] is the id of swiped [Swipe].
     *
     * ViewHolder can react here to swipes and/or return any data
     * to use for further reaction to swipes at [MultiSwipeListener]
     *
     * @return any data that will be sent
     * to [MultiSwipeListener.onSwipeDone] at [MultiSwipeAdapter].
     *
     * Returned data may be used at Fragment or Activity level.
     */
    fun onSwipeDone(swipeId: String): Any?


}