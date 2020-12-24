/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegy"
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
     * Use this method to initialize draws.
     *
     * **Caution**: Avoid creating any object in this method
     * because it will be called many times in child draw of [RecyclerView].
     * Create any object at constructor and use them here.
     *
     * [canvas] use to draw on
     *
     * [recyclerView] is current RecyclerView to draw on
     *
     * [viewHolder] is current ViewHolder
     *
     * [dX] is the amount of horizontal displacement caused by user's action
     * or by animating back
     *
     * [swipeList] is prepared (ready) swipe list
     * ([SwipeList.prepare] already is called on [swipeList])
     *
     * [swipe] is current swipe and [swipeTheme] is current swipe theme
     *
     * [hideInactiveIcons] if true inactive icons should **not** be drawn
     * only the active icon (icon of first item in [swipeList]) should be drawn
     *
     * [fromUser] True if this view is currently being controlled by the user
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
     * This method will be called after initDraw
     * and before drawIcons. Could be used to draw swipe's background.
     *
     * **Caution**: Avoid creating any object in this method
     * because it will be called many times in child draw of [RecyclerView].
     * Create any object at constructor and use them here.
     */
    fun drawBackground()

    /**
     * This method will be called after drawBackground
     * and before drawLabel. Could be used to draw swipe's icons.
     *
     * **Caution**: Avoid creating any object in this method
     * because it will be called many times in child draw of [RecyclerView].
     * Create any object at constructor and use them here.
     */
    fun drawIcons()

    /**
     * This method will be called after drawIcons
     * and before drawView. Could be used to draw swipe's label.
     *
     * **Caution**: Avoid creating any object in this method
     * because it will be called many times in child draw of [RecyclerView].
     * Create any object at constructor and use them here.
     */
    fun drawLabel()

    /**
     * This method will be called after drawLabel
     * and before drawEnds. Could be used to draw
     * a background below row's layout and above swipe icons and label.
     *
     * **Caution**: Avoid creating any object in this method
     * because it will be called many times in child draw of [RecyclerView].
     * Create any object at constructor and use them here.
     */
    fun drawView()

    /**
     * This method will be called after all other methods of this interface
     * for each frame draw.
     *
     * **Caution**: Avoid creating any object in this method
     * because it will be called many times in child draw of [RecyclerView].
     * Create any object at constructor and use them here.
     */
    fun drawEnds()

}