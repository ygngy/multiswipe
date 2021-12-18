/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegi"
 * Licensed under the MIT license
 *
 * Email: ygnegy@gmail.com
 * Github: https://github.com/ygngy
 */

package com.github.ygngy.demo.swipesample

import android.content.Context
import androidx.annotation.StringRes
import com.github.ygngy.multiswipe.Swipe

/**
 * This is a sample class for default and simple swipe creation.
 * For customized (or detailed) swipe creation see [SwipeCreator] class.
 *
 * @constructor
 * @param context Used to get resources from.
 * @param withLabel if true labels will be drawn otherwise only icons will be drawn
 */
class DefaultSwipeCreator(context: Context, private val withLabel: Boolean): SwipeCreatorBase(context) {

    override val shareSwipe = Swipe(
        context = context,
        id = SWIPE_TO_SHARE_ID,
        activeIcon = getDrawable(R.drawable.ic_baseline_share_24),
        activeLabel = getLabel(R.string.share_label),
        inactiveIcon = getDrawable(R.drawable.ic_disabled_share_24)
    )

    override val copySwipe = Swipe(
        context = context,
        id = SWIPE_TO_COPY_ID,
        activeIcon = getDrawable(R.drawable.ic_baseline_file_copy_24),
        activeLabel = getLabel(R.string.copy_label),
        inactiveIcon = getDrawable(R.drawable.ic_disabled_file_copy_24)
    )

    override val cutSwipe = Swipe(
        context = context,
        id = SWIPE_TO_CUT_ID,
        activeIcon = getDrawable(R.drawable.ic_baseline_content_cut_24),
        activeLabel = getLabel(R.string.cut_label),
        inactiveIcon = getDrawable(R.drawable.ic_disabled_content_cut_24)
    )

    override val editSwipe = Swipe(
        context = context,
        id = SWIPE_TO_EDIT_ID,
        activeIcon = getDrawable(R.drawable.ic_baseline_edit_24),
        activeLabel = getLabel(R.string.edit_label),
        inactiveIcon = getDrawable(R.drawable.ic_disabled_edit_24)
    )


    override val delSwipe = Swipe( // using Swipe short constructor
        context = context,
        id = SWIPE_TO_DEL_ID,
        activeIcon = getDrawable(R.drawable.ic_baseline_delete_24),
        activeLabel = getLabel(R.string.del_label),
        acceptIcon = getDrawable(R.drawable.ic_baseline_delete_forever_24),
        inactiveIcon = getDrawable(R.drawable.ic_disabled_delete_24)
    )

    override val likedSwipe: Swipe = Swipe(
            context = context,
            id = SWIPE_TO_LIKE_ID,
            activeIcon = getDrawable(R.drawable.ic_baseline_favorite_24),
            activeLabel = getLabel(R.string.liked_label),
            acceptIcon = getDrawable(R.drawable.ic_baseline_favorite_border_24),
            acceptLabel = getLabel(R.string.unlike_label),
            inactiveIcon = getDrawable(R.drawable.ic_disabled_favorite_24)
        )

    override val unlikedSwipe: Swipe = Swipe(
            context = context,
            id = SWIPE_TO_LIKE_ID,
            activeIcon = getDrawable(R.drawable.ic_baseline_favorite_border_24),
            activeLabel = getLabel(R.string.unliked_label),
            acceptIcon = getDrawable(R.drawable.ic_baseline_favorite_24),
            acceptLabel = getLabel(R.string.like_label),
            inactiveIcon = getDrawable(R.drawable.ic_disabled_favorite_border_24)
        )


    private fun getLabel(@StringRes stringRes: Int) =
        if (withLabel) getString(stringRes) else null

}