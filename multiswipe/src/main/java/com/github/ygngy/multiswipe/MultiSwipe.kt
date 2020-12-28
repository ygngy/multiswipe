/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegi"
 * Licensed under the MIT license
 *
 * Email: ygnegy@gmail.com
 * Github: https://github.com/ygngy
 */

package com.github.ygngy.multiswipe

/**
 * To represent swipes in correct order for left and right sides of view
 * and to handle swipes
 * [ViewHolder][androidx.recyclerview.widget.RecyclerView.ViewHolder]
 * must implements this interface.
 */
interface MultiSwipe {
    /**
     * Returns ordered swipes for left side of View.
     *
     * **Caution**: Prevent creating objects in this method
     * because this method may be called a lot.
     *
     * @return Ordered swipes. First swipe must be in first position
     * for left side of view.
     */
    val leftSwipeList: LeftSwipeList?

    /**
     * Returns ordered swipes for right side of View.
     *
     * **Caution**: Prevent creating objects in this method
     * because this method may be called a lot.
     *
     * @return Ordered swipes. First swipe must be in first position
     * for right side of view.
     */
    val rightSwipeList: RightSwipeList?



    /**
     * This method will be called when user moves enough to triggers swipe's action.
     *
     * [ViewHolder][androidx.recyclerview.widget.RecyclerView.ViewHolder]
     * can handle swipes here and/or return any data
     * to use for further handling at [MultiSwipeListener].
     *
     * @param swipeId The id of the [Swipe].
     * @return Any optional data that will be sent
     * to [MultiSwipeListener.onSwipeDone].
     */
    fun onSwipeDone(swipeId: Int): Any?


}