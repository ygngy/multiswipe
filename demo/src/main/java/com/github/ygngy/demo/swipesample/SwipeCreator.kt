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
import com.github.ygngy.multiswipe.SwipeIcon
import com.github.ygngy.multiswipe.SwipeLabel
import com.github.ygngy.multiswipe.SwipeTheme

/**
 * A sample class used to create different swipes.
 *
 * @constructor
 * @param context Used to get resources from.
 * @param liked Used to select icon and label for [likeSwipe] theme.
 */
class SwipeCreator(private val context: Context, liked: Boolean) {

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

    val shareSwipe: Swipe
    val copySwipe: Swipe
    val cutSwipe: Swipe
    val likeSwipe: Swipe
    val editSwipe: Swipe
    val delSwipe: Swipe

    init {

        val shareLabel = SwipeLabel(
                text = getString(R.string.share_label),
                textColor = getColor(R.color.swipe_text),
                textSize = getDimension(R.dimen.swipe_text_size),
                margin = getDimension(R.dimen.swipe_text_margin),
        )

        val shareIcon = SwipeIcon(drawable = getDrawable(R.drawable.ic_baseline_share_24)!!,
                edgeHorMargin = getDimension(R.dimen.swipe_edge_margin),
                iconHorMargin = getDimension(R.dimen.swipe_icon_margin),
                tailHorMargin = getDimension(R.dimen.swipe_tail_margin))

        // To prevent code repeat,
        // i use this theme as base theme and extend it for other themes
        val shareTheme = SwipeTheme (
                icon = shareIcon,
                label = shareLabel,
                backgroundColor = getColor(R.color.swipe_background),
                viewColor = getColor(R.color.view_background_color)
        )

        val shareAcceptTheme = shareTheme.copy(
                backgroundColor = getColor(R.color.swipe_accept_background),
                label = shareLabel.copy(textColor = getColor(R.color.swipe_accept_text)),
                viewColor = getColor(R.color.view_background_accept_color)
        )

        val copyTheme = shareTheme.extend(
                R.drawable.ic_baseline_file_copy_24, R.string.copy_label)

        val copyAcceptTheme = shareAcceptTheme.extend(
                R.drawable.ic_baseline_file_copy_24, R.string.copy_label
        )

        val cutTheme = shareTheme.extend(
               R.drawable.ic_baseline_content_cut_24, R.string.cut_label
        )

        val cutAcceptTheme = shareAcceptTheme.extend(
                R.drawable.ic_baseline_content_cut_24, R.string.cut_label
        )

        val likeTheme = if (liked) shareTheme.extend(
                R.drawable.ic_baseline_favorite_24,
                R.string.liked_label)
        else shareTheme.extend(
                R.drawable.ic_baseline_favorite_border_24,
                R.string.unliked_label)

        val likeAcceptTheme = if (liked) shareAcceptTheme.extend(
                R.drawable.ic_baseline_favorite_border_24,
                R.string.unlike_label)
        else shareAcceptTheme.extend(
                R.drawable.ic_baseline_favorite_24,
                R.string.like_label)


        val editTheme = shareTheme.extend(
                R.drawable.ic_baseline_edit_24, R.string.edit_label
        )

        val editAcceptTheme = shareAcceptTheme.extend(
                R.drawable.ic_baseline_edit_24, R.string.edit_label
        )

        val delTheme = shareTheme.extend(
                R.drawable.ic_baseline_delete_24, R.string.del_label
        )

        val delAcceptTheme = shareAcceptTheme.extend(
                R.drawable.ic_baseline_delete_forever_24, R.string.del_label
        )

        shareSwipe = Swipe(
                id = SWIPE_TO_SHARE_ID,
                activeTheme = shareTheme,
                acceptTheme = shareAcceptTheme,
                inactiveIcon = getDrawable(R.drawable.ic_disabled_share_24)
         )

        copySwipe = Swipe(
                id = SWIPE_TO_COPY_ID,
                activeTheme = copyTheme,
                acceptTheme = copyAcceptTheme,
                inactiveIcon = getDrawable(R.drawable.ic_disabled_file_copy_24)
        )

        cutSwipe = Swipe(
                id = SWIPE_TO_CUT_ID,
                activeTheme = cutTheme,
                acceptTheme = cutAcceptTheme,
                inactiveIcon = getDrawable(R.drawable.ic_disabled_content_cut_24)
        )

        likeSwipe = Swipe(
                id = SWIPE_TO_LIKE_ID,
                activeTheme = likeTheme,
                acceptTheme = likeAcceptTheme,
                inactiveIcon = getDrawable(
                if (liked) R.drawable.ic_disabled_favorite_24
                else R.drawable.ic_disabled_favorite_border_24)
        )

        editSwipe = Swipe(
                id = SWIPE_TO_EDIT_ID,
                activeTheme = editTheme,
                acceptTheme = editAcceptTheme,
                inactiveIcon = getDrawable(R.drawable.ic_disabled_edit_24)
        )

        delSwipe = Swipe(
                id = SWIPE_TO_DEL_ID,
                activeTheme = delTheme,
                acceptTheme = delAcceptTheme,
                inactiveIcon = getDrawable(R.drawable.ic_disabled_delete_24)
        )

    }

    /**
     * A sample method used to prevent code repeat when creating [SwipeTheme].
     *
     * @receiver [SwipeTheme] To extend and change its icon and text.
     * @param drawableRes Drawable resource id of icon in [SwipeTheme].
     * @param stringRes String resource id of label in [SwipeTheme].
     */
    private fun SwipeTheme.extend(@DrawableRes drawableRes: Int,
                                  @StringRes stringRes: Int): SwipeTheme = copy(
                    icon = icon.copy(drawable = getDrawable(drawableRes)!!),
                    label = label?.copy(text = getString(stringRes))
            )

    /**
     * This method is used to shorten [ContextCompat.getDrawable]
     * because it will be used a lot here.
     */
    private fun getDrawable(@DrawableRes drawableRes: Int) =
            ContextCompat.getDrawable(context, drawableRes)

    /**
     * This method is used to shorten [ContextCompat.getColor]
     * because it will be used a lot here.
     */
    private fun getColor(@ColorRes colorRes: Int) =
            ContextCompat.getColor(context, colorRes)

    /**
     * This method is used to shorten [Context.getString]
     * because it will be used a lot here.
     */
    private fun getString(@StringRes stringRes: Int) =
            context.getString(stringRes)

    /**
     * This method is used to shorten
     * [context.resources.getDimension][android.content.res.Resources.getDimension]
     * because it will be used a lot here.
     */
    private fun getDimension(@DimenRes dimenRes: Int) =
            context.resources.getDimension(dimenRes)
}