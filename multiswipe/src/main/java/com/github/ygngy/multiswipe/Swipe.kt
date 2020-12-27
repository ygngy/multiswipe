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
 * Represents icons and text on [backgroundColor]
 * below each [RecyclerView][androidx.recyclerview.widget.RecyclerView] row.
 *
 * @constructor [backgroundColor] will be drawn fully below icons and texts
 * and rows of recyclerView. But [viewColor] is used to draw a background
 * below swiped view and **above** [label], [icon] and [backgroundColor].
 * [viewColor] will be hidden below view's background if view has a background.
 * [icon] contains swipe's icon and margin between icons.
 * [label] is used to clarifying the icon action better.
 *
 * @property icon Swipe's icon and margin between icons.
 * @property label Used to clarify the icon's action better.
 * [label] Will be drawn only if there is enough height
 * to draw text based on [SwipeLabel.textSize].
 * @property backgroundColor Wll be drawn fully below icons and texts and rows of recyclerView.
 * @property viewColor Used to draw a background
 * below swiped view and **above** [label], [icon] and [backgroundColor].
 * It will be hidden below view's background if view has a background.
 */
data class SwipeTheme
@JvmOverloads constructor(val icon: SwipeIcon,
                          val label: SwipeLabel? = null,
                          val backgroundColor: Int = Color.TRANSPARENT,
                          val viewColor: Int = Color.TRANSPARENT){

    /**
     * @param position position of margin:
     * **0** for edge (margin between edge and first icon),
     * **1** for main icon (margin between first and second icon) and
     * **2** for other icons (margin between other icons)
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
 * Represents swipe text, text color, text size and text margin.
 * This class is used to clarifying the icon's action better and is used for [SwipeTheme].
 *
 * @property text Text of [SwipeLabel].
 * @property textColor Color of [text] in [SwipeLabel].
 * @property textSize Size of [text] in [SwipeLabel].
 * @property margin Horizontal margin between text and last icon
 * when this [SwipeLabel] is shown.
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
 * Represents swipe icon and margins for icons and is used for [SwipeTheme].
 *
 * @constructor [drawable] Drawable will be drawn as swipe icon.
 * [edgeHorMargin] Horizontal margin between first icon and edge of view.
 * [iconHorMargin] Horizontal margin between first icon and second icon (if there is second icon).
 * [tailHorMargin] Horizontal margin between inactive icons (if there is more than one inactive icon)
 *
 * @property drawable Drawable will be drawn as swipe icon.
 * @property edgeHorMargin Horizontal margin between first icon and edge of view.
 * @property iconHorMargin Horizontal margin between first icon and second icon (if there is second icon).
 * @property tailHorMargin Horizontal margin between inactive icons (if there is more than one inactive icon)
 */
data class SwipeIcon(val drawable: Drawable,
                     val edgeHorMargin: Float,
                     val iconHorMargin: Float,
                     val tailHorMargin: Float)

/**
 * Is used to draw each swipe on screen based on its state.
 * Represents swipe id, themes and icons.
 *
 * @constructor [id] is used to identify this specific swipe.
 * [activeTheme] is used when this swipe is active.
 * [acceptTheme] is used when the user has moved enough to trigger swipe's action.
 * [inactiveIcon] is to draw currently inactive icons (another swipe is active).
 * **Note**: If [acceptTheme] and/or [inactiveIcon] is null they will be same as [activeTheme].
 *
 * @property id Used to notify listeners which swipe is triggered and to remember last triggered swipe.
 * @property activeTheme Swipe theme used when this swipe is active but is not accept
 * (user has not moved enough to trigger swipe's action).
 * @property acceptTheme The Swipe theme used when the user has moved enough to trigger swipe's action.
 * @property inactiveIcon Drawable used to draw currently inactive icons (another swipe is active).
 */
data class Swipe @JvmOverloads constructor(val id: String,
                 val activeTheme: SwipeTheme,
                 val acceptTheme: SwipeTheme? = null,
                 val inactiveIcon: Drawable? = null) {

    /**
     * Returns correct theme for active or accept swipes.
     *
     * @param isAccepted Must be true if this swipe is in accept state.
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