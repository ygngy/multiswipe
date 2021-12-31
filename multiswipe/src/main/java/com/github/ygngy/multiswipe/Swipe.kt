/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegi"
 * Licensed under the MIT license
 *
 * Email: ygnegy@gmail.com
 * Github: https://github.com/ygngy
 */

package com.github.ygngy.multiswipe

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import com.github.ygngy.multiswipe.SwipeHistory.Companion.isValidSwipeId

/**
 * Represents colors, icons and text
 * below each [RecyclerView][androidx.recyclerview.widget.RecyclerView] row.
 *
 * @constructor [backgroundColor] will be drawn fully below icons and texts
 * and rows of recyclerView. But [viewColor] is used to draw a background
 * below swiped view and **above** [label], [icon] and [backgroundColor].
 * But [viewColor] will be hidden below view's background if view has a background.
 * [icon] contains swipe's icon and margin between icons.
 * [label] is optional and is used to clarifying the icon action better.
 * **In this constructor default [backgroundColor] and [viewColor] is transparent.**
 * So if you want to use this constructor showing background color you need to pass it to this constructor.
 * If you want to use default background colors (green and red) you may use other constructors with [Context] argument.
 *
 * @property icon Swipe's icon and margin between icons.
 * @property label Used to clarify the icon's action better.
 * [label] Will be drawn only if there is enough height
 * to draw text based on [SwipeLabel.textSize].
 * @property backgroundColor Wll be drawn fully below [icon] and [label] and rows of recyclerView (default is transparent).
 * @property viewColor Used to draw a background (default is transparent)
 * below swiped view and **above** [label], [icon] and [backgroundColor].
 * But it will be hidden below view's background if view has a background.
 */
data class SwipeTheme
@JvmOverloads constructor(val icon: SwipeIcon,
                          val label: SwipeLabel? = null,
                          val backgroundColor: Int = Color.TRANSPARENT,
                          val viewColor: Int = Color.TRANSPARENT){

    /**
     * This constructor uses default colors and margins to simplify theme creation.
     * In this constructor default [backgroundColor] is green or red based on [isAcceptTheme] .
     * Default [viewColor] is pale green or pale red (based on [isAcceptTheme]) in Android day theme and gray or black in Android night (dark) theme.
     * Default [labelColor] is pale green or pale red based on [isAcceptTheme].
     *
     * @param context is used to extract default values from resources.
     * @param icon a drawable used to draw swipe icon
     * @param label optional label to explain swipe action. Label will be hidden if height is to small for label.
     * @param isAcceptTheme used to select default colors for label and backgrounds (default is false). Pass true value if you want to apply this theme when swipe displacement is more than accept boundary
     * @param labelColor color used to draw label (if [isAcceptTheme] is true default is pale red otherwise default is pale green)
     * @param backgroundColor color used to draw background below icon and label (if [isAcceptTheme] is true default is red otherwise default is green)
     * @param viewColor color used to draw view background (in day theme of Android if [isAcceptTheme] is true default is pale red otherwise is pale green. in night theme of Android view background may be gray or black)
     */
    @JvmOverloads
    constructor(
        context: Context,
        icon: Drawable,
        label: String? = null,
        isAcceptTheme: Boolean = false,
        labelColor: Int = getColor(context, if (isAcceptTheme) R.color.mswipe_accept_text else R.color.mswipe_text),
        backgroundColor: Int = getColor(context, if (isAcceptTheme) R.color.mswipe_background_accept else R.color.mswipe_background),
        viewColor: Int = getColor(context, if (isAcceptTheme) R.color.mswipe_view_background_accept else R.color.mswipe_view_background)
    ): this(
        SwipeIcon(context, icon),
        if (label == null) null else SwipeLabel(context, label, isAcceptTheme, labelColor),
        backgroundColor,
        viewColor
    )

    /**
     * @param position position of margin:
     * **0** for edge (margin between edge and active icon),
     * **1** for main icon (margin between active icon and first inactive icon)
     * **2** for other icons (margin between inactive icons)
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
 * @constructor [text] is text of this label.
 * [textColor] is color of [text] in [SwipeLabel].
 * [textSize] is Size of [text] in [SwipeLabel].
 * But in case of too large [textSize] for height of view, label will be hidden.
 * [margin] is Horizontal margin between [text] and the icon next to it, when this [SwipeLabel] is shown.
 *
 *
 * @property text Text of [SwipeLabel].
 * @property textColor Color of [text] in [SwipeLabel].
 * @property textSize Size of [text] in [SwipeLabel]. In case of too large [textSize] for height of view, label will be hidden.
 * @property margin Horizontal margin between [text] and the icon next to it, when this [SwipeLabel] is shown.
 */
data class SwipeLabel(val text: String,
                      val textColor: Int,
                      val textSize: Float,
                      val margin: Float) {

    /**
     * This constructor is same as main constructor
     * but in this constructor you may omit some argument and use defaults.
     *
     * @param context is used to extract default values from resources.
     * @param text Text of [SwipeLabel].
     * @param isAcceptLabel is used to define text color if no value for [textColor] is provided. Use true for pale red and false for pale green.
     * @param textColor Color of [text] in [SwipeLabel].
     * @param textSize Size of [text] in [SwipeLabel].
     * @param margin Horizontal margin between text and the icon next to it, when this [SwipeLabel] is shown.
     */
    @JvmOverloads
    constructor(context: Context,
                text: String,
                isAcceptLabel: Boolean = false,
                textColor: Int = getColor(context, if (isAcceptLabel) R.color.mswipe_accept_text else R.color.mswipe_text),
                textSize: Float = getDimen(context, R.dimen.mswipe_text_size),
                margin: Float = getDimen(context, R.dimen.mswipe_text_margin)
    ): this(text, textColor, textSize, margin)

    init {
        if (textSize <= 0) throw IllegalArgumentException("Text Size must be positive value")
    }

    override fun toString(): String {
        return text
    }
}

/**
 * Represents swipe icon and icon margins and is used for [SwipeTheme].
 *
 * @constructor [drawable] Drawable will be drawn as swipe icon.
 * [edgeHorMargin] Horizontal margin between [drawable] and edge of view.
 * [iconHorMargin] Horizontal margin between [drawable] and first inactive icon (if there is any other icons).
 * [tailHorMargin] Horizontal margin between inactive icons (if there is more than one inactive icon)
 *
 * @property drawable Drawable will be drawn as swipe icon.
 * @property edgeHorMargin Horizontal margin between [drawable] and edge of view.
 * @property iconHorMargin Horizontal margin between [drawable] and first inactive icon (if there is any other icons).
 * @property tailHorMargin Horizontal margin between inactive icons (if there is more than one inactive icon)
 */
data class SwipeIcon(val drawable: Drawable,
                     val edgeHorMargin: Float,
                     val iconHorMargin: Float,
                     val tailHorMargin: Float){

    /**
     * This constructor is same as main constructor
     * but in this constructor you may omit some argument and use defaults.
     *
     * @param context is used to extract default values from resources.
     * @param drawable Drawable will be drawn as swipe icon.
     * @param edgeHorMargin Horizontal margin between [drawable] and edge of view.
     * @param iconHorMargin Horizontal margin between [drawable] and first inactive icon (if there is any other icons).
     * @param tailHorMargin Horizontal margin between inactive icons (if there is more than one inactive icon)
     */
    @JvmOverloads
    constructor(
        context: Context,
        drawable: Drawable,
        edgeHorMargin: Float = getDimen(context, R.dimen.mswipe_edge_margin),
        iconHorMargin: Float = getDimen(context, R.dimen.mswipe_icon_margin),
        tailHorMargin: Float = getDimen(context, R.dimen.mswipe_tail_margin)
    ): this(drawable, edgeHorMargin, iconHorMargin, tailHorMargin)
}

/**
 * Is used to draw each swipe on screen based on its state.
 * Represents swipe id, themes and icons.
 *
 * @constructor [id] is used to identify this specific swipe.
 * **[id] Must be greater than zero or [IllegalArgumentException] will be thrown.**
 * [activeTheme] is used when this swipe is active.
 * [acceptTheme] is used when the swipe displacement is more than accept boundary.
 * [inactiveIcon] is used to draw this swipe when this swipe is inactive.
 * **Note**: Default value for [acceptTheme] and [inactiveIcon] is the value of [activeTheme].
 *
 * @property id Is used to notify listeners which swipe is triggered and to remember last triggered swipe.
 * **[id] Must be greater than zero or [IllegalArgumentException] will be thrown**.
 * @property activeTheme is used when this swipe is active but swipe displacement is less than accept boundary.
 * @property acceptTheme Is used when swipe displacement is more than accept boundary.
 * @property inactiveIcon Used to draw this swipe when this swipe is inactive.
 * **Note:** In each swipe move only one swipe is active (or accept) and other swipes are inactive
 * from inactive swipes only [inactiveIcon] will be drawn with active (or accept) theme of active (or accept) swipe.
 */
data class Swipe @JvmOverloads constructor(
                 val id: Int,
                 val activeTheme: SwipeTheme,// openingTheme
                 val acceptTheme: SwipeTheme = activeTheme,// expandTheme
                 val inactiveIcon: Drawable = activeTheme.icon.drawable) {// lineupIcon or queueIcon

    /***
     * Simple Swipe constructor to use default themes
     *
     * @param context is used to extract default values from resources.
     * @param id Used to notify listeners which swipe is triggered and to remember last triggered swipe.
     * **[id] Must be greater than zero or [IllegalArgumentException] will be thrown**.
     * @param activeIcon icon used for [activeTheme] (when this swipe is active but its displacement is less than accept boundary)
     * @param activeLabel label used for [activeTheme] (when this swipe is active but its displacement is less than accept boundary)
     * @param acceptIcon icon used for [acceptTheme] (when this swipe displacement is more than accept boundary)
     * @param acceptLabel label used for [acceptTheme] (when this swipe displacement is more than accept boundary)
     * @param inactiveIcon icon used when this swipe is not in active or accept state (another swipe is in active or accept state)
     *
     */
    @JvmOverloads
    constructor(context: Context,
                id: Int,
                activeIcon: Drawable,
                activeLabel: String? = null,
                acceptIcon: Drawable = activeIcon,
                acceptLabel: String? = activeLabel,
                inactiveIcon: Drawable = activeIcon)
            : this(
            id,
            activeTheme = SwipeTheme(context, activeIcon, activeLabel, isAcceptTheme = false),
            acceptTheme = SwipeTheme(context, acceptIcon, acceptLabel, isAcceptTheme = true),
            inactiveIcon
        )


    init {
        // In this library negative or zero ids for swipes is reserved as no id in swipe history.
        if (!isValidSwipeId(id))
            throw IllegalArgumentException("The swipe id: $id is Invalid. It must be greater than zero.")
    }

}

/**
 * This method is used to shorten [ContextCompat.getColor]
 * because it will be used a lot here.
 */
private fun getColor(context: Context, @ColorRes colorRes: Int) =
    ContextCompat.getColor(context, colorRes)
/**
 * This method is used to shorten
 * [context.resources.getDimension][android.content.res.Resources.getDimension]
 * because it will be used a lot here.
 */
private fun getDimen(context: Context, @DimenRes dimenRes: Int) =
    context.resources.getDimension(dimenRes)

