/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegi"
 * Licensed under the MIT license
 *
 * Email: ygnegy@gmail.com
 * Github: https://github.com/ygngy
 */

package com.github.ygngy.multiswipe

sealed class SwipeList(private val history: SwipeHistory,
                     private vararg val list: Swipe): Iterator<Swipe> {

    private var head = 0
    var position = -1
        private set

    val size get() = list.size
    val isEmpty get() = list.isEmpty()
    val isNotEmpty get() = list.isNotEmpty()

    val activeSwipe get() = if (isNotEmpty) list[head] else null

    /**
     * When creating list if previously user has selected a special swipe
     * (and that swipe is presented in the current list)
     * it will be active swipe ignoring current list order. To change
     * history of active swipe use this method.
     * @return true if history changed.
     */
    fun changeHistory(swipeId: String): Boolean{
        val oldHistory = history.id
        history.id = swipeId
        return oldHistory != history.id
    }

    /**
     * Initializes head index based on [id]
     * @return true if head changed
     */
    private fun setAsHead(id: String?): Boolean {
        if (id != null)
            list.forEachIndexed { i, swp ->
                if (swp.id == id) {
                    head = i
                    return true
                }
            }
        return false
    }

    /**
     * Finds next Swipe and resets iteration
     * Must be called when user starts to swipe
     *
     * @return this object
     */
    fun prepare(): SwipeList {
        if (isNotEmpty) {
            history.id = when {
                setAsHead(history.id) -> history.id
                setAsHead(history.opposite.id) -> history.opposite.id
                else -> list[head].id
            }
        }
        position = -1
        return this
    }

    /**
     * Shifts from current swipe to next swipe
     * and resets iteration
     *
     * Must be called when user release finger before swiping enough to
     * trigger swipes action (when user cancels swipe).
     *
     * @return this object
     */
    fun shift(): SwipeList {
        prepare()
        if (isNotEmpty){
            head = (head + 1) % list.size
            history.id = list[head].id
        }
        position = -1
        return this
    }


    override fun hasNext(): Boolean = position < list.lastIndex

    override fun next(): Swipe =
        if (hasNext()) {
            ++position// going to next position
            list[(head + position) % list.size]
        } else throw IndexOutOfBoundsException()

}

/**
 * Swipes for left side of rows of recycler view.
 * Swipes argument order is the order of swipes.
 * First argument will be first and active swipe
 * if there is no swipe at history.
 */
class LeftSwipeList(vararg swipes: Swipe): SwipeList(LeftSwipeHistory, *swipes)
/**
 * Swipes for right side of rows of recycler view.
 * Swipes argument order is the order of swipes.
 * First argument will be first and active swipe
 * if there is no swipe at history.
 */
class RightSwipeList(vararg swipes: Swipe): SwipeList(RightSwipeHistory, *swipes)