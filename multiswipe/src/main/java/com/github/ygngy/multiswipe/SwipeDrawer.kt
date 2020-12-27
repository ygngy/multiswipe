/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegi"
 * Licensed under the MIT license
 *
 * Email: ygnegy@gmail.com
 * Github: https://github.com/ygngy
 */

package com.github.ygngy.multiswipe

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView

/**
 * This interface is used to draw swipe frames.
 * This interface is already implemented at [SwipeDrawerImpl].
 * To change some of draws you can extend [SwipeDrawerImpl]
 * instead of implementing this interface.
 */
interface SwipeDrawer {

    /**
     * This method will be called for each frame draw before other methods
     * of this interface.
     * Use this method to initialize each frame draw.
     *
     * **Caution**: Avoid creating any object in this method
     * because it will be called a lot in child draw of [RecyclerView].
     * Create any object at constructor and use them here.
     *
     * @param canvas Used to draw on.
     * @param recyclerView Current RecyclerView to draw on.
     * @param viewHolder Current ViewHolder.
     * @param dX The amount of horizontal displacement caused by user's action
     * or by animating back.
     * @param swipeList Prepared (ready) swipe list
     * ([SwipeList.prepare] is called on it).
     * @param swipe Current swipe.
     * @param swipeTheme Current swipe theme.
     * @param hideInactiveIcons If true inactive icons should **not** be drawn
     * only the active icon (icon of first item in [swipeList]) should be drawn.
     * @param fromUser Is true if this view is currently being controlled by the user
     * or false if it is simply animating back to its original state.
     */
    fun initDraw(canvas: Canvas,
                 recyclerView: RecyclerView,
                 viewHolder: RecyclerView.ViewHolder,
                 dX: Float,
                 swipeList: SwipeList, swipe: Swipe,
                 swipeTheme: SwipeTheme,
                 hideInactiveIcons: Boolean,
                 fromUser: Boolean)

    /**
     * Could be used to draw swipe's background.
     *
     * This method will be called after [initDraw]
     * and before [drawIcons].
     *
     * **Caution**: Avoid creating any object in this method
     * because it will be called a lot in child draw of [RecyclerView].
     * Create any object at constructor and use them here.
     */
    fun drawBackground()

    /**
     * Could be used to draw swipe's icons.
     *
     * This method will be called after [drawBackground]
     * and before [drawLabel].
     *
     * **Caution**: Avoid creating any object in this method
     * because it will be called a lot in child draw of [RecyclerView].
     * Create any object at constructor and use them here.
     */
    fun drawIcons()

    /**
     * Could be used to draw swipe's label.
     *
     * This method will be called after [drawIcons]
     * and before [drawView].
     *
     * **Caution**: Avoid creating any object in this method
     * because it will be called a lot in child draw of [RecyclerView].
     * Create any object at constructor and use them here.
     */
    fun drawLabel()

    /**
     * Could be used to draw a background below row's layout
     * and **above** swipe icons and label.
     *
     * This method will be called after [drawLabel]
     * and before [drawEnds].
     *
     * **Caution**: Avoid creating any object in this method
     * because it will be called a lot in child draw of [RecyclerView].
     * Create any object at constructor and use them here.
     */
    fun drawView()

    /**
     * Could be used for memory cleanup or other draw endings.
     *
     * This method will be called after all other methods of this interface
     * for each frame draw.
     *
     * **Caution**: Avoid creating any object in this method
     * because it will be called a lot in child draw of [RecyclerView].
     * Create any object at constructor and use them here.
     */
    fun drawEnds()

}