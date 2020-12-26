/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegi"
 * Licensed under the MIT license
 *
 * Email: ygnegy@gmail.com
 * Github: https://github.com/ygngy
 */

package com.github.ygngy.multiswipe

import android.graphics.Color
import android.graphics.drawable.Drawable

/**
 * Represents each icon and text on [backgroundColor]
 * below each recyclerView row.
 *
 * [icon] swipe's icon and margin between icons.
 *
 * [label] is used to clarifying the icon action better.
 * [label] will be drawn only if there is enough height
 * to draw text based on [SwipeLabel.textSize].
 *
 * [backgroundColor] must be the color's code.
 * This color will be drawn fully below icons and texts and rows of recyclerView.
 *
 * [viewColor] color value used to draw a background
 * below swiped view and **above** [label], [icon] and [backgroundColor].
 * It will be hidden below view's background if view has a background.
 */
data class SwipeTheme
@JvmOverloads constructor(val icon: SwipeIcon,
                          val label: SwipeLabel? = null,
                          val backgroundColor: Int = Color.TRANSPARENT,
                          val viewColor: Int = Color.TRANSPARENT){

    /**
     * [position] position of margin: 0 for edge, 1 for main icon, 2 for other icons
     *
     * @return Horizontal margin for each position
     */
    fun getHorizontalMargin(position: Int): Float = when(position) {
        0 -> icon.edgeHorMargin
        1 -> icon.iconHorMargin
        else -> icon.tailHorMargin
    }
}


/**
 * This class is used to clarifying the icon's action better.
 *
 * [text] is label's text.
 *
 * [textColor] is color of [text]
 *
 * [textSize] is size of [text]
 *
 * [margin] horizontal margin between text and last icon
 * when this label is shown.
 */
data class SwipeLabel(val text: String,
                      val textColor: Int,
                      val textSize: Float,
                      val margin: Float) {

    init {
        if (textSize <= 0) throw IllegalArgumentException("Text Size must be positive value")
    }

    override fun toString(): String {
        return text
    }
}

/**
 * [SwipeIcon] is used for [SwipeTheme].
 *
 * [drawable] Drawable will be drawn as swipe icon.
 *
 * [edgeHorMargin] Horizontal margin between first icon and edge of view.
 *
 * [iconHorMargin] Horizontal margin between first icon and second icon (if there is second icon).
 *
 * [tailHorMargin] Horizontal margin between inactive icons (if there is more than one inactive icon)
 */
data class SwipeIcon(val drawable: Drawable,
                     val edgeHorMargin: Float,
                     val iconHorMargin: Float,
                     val tailHorMargin: Float)

/**
 * [Swipe] is used to draw each swipe on screen based on its state:
 * 
 * [id] is used to notify client which swipe is triggered and to remember last triggered swipe.
 *
 * [activeTheme]: Swipe theme used when this swipe is active but is not accept
 * (user has not swiped enough to trigger swipe's action).
 *
 * [acceptTheme]: The Swipe theme used when
 * the user has swiped enough to trigger swipe's action.
 *
 * [inactiveIcon]: The icon used to draw currently inactive icons (another swipe is active).
 *
 * **Note**: If [acceptTheme] and/or [inactiveIcon] is null they will be same as [activeTheme].
 */
data class Swipe @JvmOverloads constructor(val id: String,
                 val activeTheme: SwipeTheme,
                 val acceptTheme: SwipeTheme? = null,
                 val inactiveIcon: Drawable? = null) {

    /**
     * Returns correct theme for active or accept swipes.
     *
     * @return [acceptTheme] if [acceptTheme] is not null
     * and [isAccepted] is true otherwise returns [activeTheme]
     */
    fun getTheme(isAccepted: Boolean): SwipeTheme =
        if (isAccepted && acceptTheme != null) acceptTheme
        else activeTheme

    /**
     * Gets a drawable to draw as inactive icon
     *
     * @return [inactiveIcon] if it is not null
     * otherwise returns activeTheme's icon
     */
    val inactiveDrawable get() = inactiveIcon ?: activeTheme.icon.drawable
}