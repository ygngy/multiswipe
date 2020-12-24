/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegy"
 * Licensed under the MIT license
 *
 * Email: ygnegy@gmail.com
 * Github: https://github.com/ygngy
 */

package com.github.ygngy.multiswipe

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import org.junit.Test
import org.junit.Assert.*

class SwipeListTest {
    // This drawable should be a different icon for each SwipeTheme
    // but here for testing i am using a fake drawable
    private val drawable = ColorDrawable(Color.TRANSPARENT)

    private fun mockSwipe(id: String) =
        Swipe(id, SwipeTheme(SwipeIcon(drawable, 16f,8f,4f)))

    @Test
    fun shiftTest() {
        fun getOrderIndex( repeat: Int, index: Int): Int{
            val indices = arrayOf(
                arrayOf(0, 1, 2, 3),
                arrayOf(1, 2, 3, 0),
                arrayOf(2, 3, 0, 1),
                arrayOf(3, 0, 1, 2)
            )
            return indices[repeat % 4][index]
        }
        val array = arrayOf(mockSwipe("share"), mockSwipe("copy"),
        mockSwipe("cut"), mockSwipe("edit"))

        val swipeList = LeftSwipeList(*array).prepare()

        assertEquals(true, swipeList.isNotEmpty)
        assertEquals(false, swipeList.isEmpty)
        assertEquals("share", swipeList.activeSwipe?.id)

        repeat(20){ testNum ->
            var i = 0
            swipeList.forEach { swp ->
                assertEquals(array[getOrderIndex(testNum, i)].id, swp.id)
                i++
            }
            swipeList.shift()
            assertEquals(4, i)
        }
        assertNotEquals("edit", swipeList.activeSwipe?.id)
        assertEquals(true, swipeList.changeHistory("edit"))
        swipeList.prepare()
        assertEquals("edit", swipeList.activeSwipe?.id)
    }

}