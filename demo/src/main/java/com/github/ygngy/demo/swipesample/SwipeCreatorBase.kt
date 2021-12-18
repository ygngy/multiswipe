/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegi"
 * Licensed under the MIT license
 *
 * Email: ygnegy@gmail.com
 * Github: https://github.com/ygngy
 */

package com.github.ygngy.demo.swipesample

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.github.ygngy.multiswipe.Swipe

/**
 * Base class for swipe creation classes
 *
 * @constructor
 * @param context Used to get resources from.
 */
abstract class SwipeCreatorBase(private val context: Context) {

    companion object{
        // swipe ids must be positive
        // or IllegalArgumentException will be thrown
        const val SWIPE_TO_SHARE_ID = 1
        const val SWIPE_TO_COPY_ID = 2
        const val SWIPE_TO_CUT_ID = 3
        const val SWIPE_TO_LIKE_ID = 4
        const val SWIPE_TO_EDIT_ID = 5
        const val SWIPE_TO_DEL_ID = 6
    }


    abstract val shareSwipe: Swipe
    abstract val copySwipe: Swipe
    abstract val cutSwipe: Swipe
    abstract val editSwipe: Swipe
    abstract val delSwipe: Swipe
    abstract val likedSwipe: Swipe
    abstract val unlikedSwipe: Swipe

    fun getLikeSwipe(liked: Boolean): Swipe
            = if (liked) likedSwipe else unlikedSwipe

    /**
     * This method is used to shorten [ContextCompat.getDrawable]
     * because it will be used a lot in subclasses.
     */
    protected fun getDrawable(@DrawableRes drawableRes: Int) =
        ContextCompat.getDrawable(context, drawableRes)!!

    /**
     * This method is used to shorten [ContextCompat.getColor]
     * because it will be used a lot in subclasses.
     */
    protected fun getColor(@ColorRes colorRes: Int) =
        ContextCompat.getColor(context, colorRes)

    /**
     * This method is used to shorten [Context.getString]
     * because it will be used a lot in subclasses.
     */
    protected fun getString(@StringRes stringRes: Int) =
        context.getString(stringRes)

    /**
     * This method is used to shorten
     * [context.resources.getDimension][android.content.res.Resources.getDimension]
     * because it will be used a lot in subclasses.
     */
    protected fun getDimension(@DimenRes dimenRes: Int) =
        context.resources.getDimension(dimenRes)
}