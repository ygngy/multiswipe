/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegi"
 * Licensed under the MIT license
 *
 * Email: ygnegy@gmail.com
 * Github: https://github.com/ygngy
 */

package com.github.ygngy.multiswipe

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import androidx.annotation.FloatRange
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.lang.ClassCastException
import kotlin.math.abs

/**
 * Shorthand function that could be used to attach [MultiSwipeAdapter]
 * to [RecyclerView]
 *
 * @param listener An optional listener to get events about swipes.
 * @param supportsRtl If is true and layout direction is RTL
 * left swipe list will be used at right side and right list at left side of each row
 * (default value: false).
 * @param hideInactiveIcons If true when swipe is in accepting state
 * inactive icons will be hidden (default value: true).
 * @param swipeThreshold The fraction that the user should move the View to be considered as swiped.
 * The fraction is calculated with respect to View's bounds.
 * Default value is .5f, which means, to swipe a View, user must move the View
 * at least half of View's width. This value mus be between 0 and 1
 * (default value: .5F).
 * @param drawer Used to draw backgrounds, icons and labels for swipes.
 * To change draws use a custom subclass of [SwipeDrawerImpl] or implementation of [SwipeDrawer].
 */
@JvmOverloads
fun RecyclerView.multiSwiping(
        listener: MultiSwipeListener? = null,
        supportsRtl: Boolean = false,
        hideInactiveIcons: Boolean = true,
        @FloatRange(from = 0.0, to = 1.0, fromInclusive = false, toInclusive = false)
        swipeThreshold: Float = .5F,
        drawer: SwipeDrawer = SwipeDrawerImpl()

) = ItemTouchHelper(MultiSwipeAdapter(
        listener = listener,
        supportsRtl = supportsRtl,
        hideInactiveIcons = hideInactiveIcons,
        swipeThreshold =  swipeThreshold,
        drawer = drawer)).attachToRecyclerView(this)

/**
 * This class could be attached to [RecyclerView] using [ItemTouchHelper]:
 *
 * ItemTouchHelper(MultiSwipeAdapter(...)).attachToRecyclerView(recyclerView)
 *
 * @constructor Default constructor to instantiate [MultiSwipeAdapter].
 * @param listener An optional listener to get events about swipes.
 * @param supportsRtl If is true and layout direction is RTL
 * left swipe list will be used at right side and right list at left side of each row
 * (default value: false).
 * @param hideInactiveIcons If true when swipe is in accepting state inactive icons will be hidden
 * (default value: true).
 * @param swipeThreshold The fraction that the user should move the View to be considered as swiped.
 * The fraction is calculated with respect to View's bounds.
 * Default value is .5f, which means, to swipe a View, user must move the View
 * at least half of View's width. This value mus be between 0 and 1
 * (default value: .5F).
 * @param drawer Used to draw backgrounds, icons and labels for swipes.
 * To change draws use a custom subclass of [SwipeDrawerImpl] or implementation of [SwipeDrawer].
 */
@SuppressLint("ClickableViewAccessibility")
open class MultiSwipeAdapter @JvmOverloads constructor(
        private val listener: MultiSwipeListener? = null,
        private val supportsRtl: Boolean = false,
        private val hideInactiveIcons: Boolean = true,
        @FloatRange(from = 0.0, to = 1.0, fromInclusive = false, toInclusive = false)
        private val swipeThreshold: Float = .5F,
        private val drawer: SwipeDrawer = SwipeDrawerImpl()
        ) : ItemTouchHelper.Callback(), View.OnTouchListener {

    init {
        if (swipeThreshold <= 0 || swipeThreshold >= 1)
            throw IllegalArgumentException("swipeThreshold must be between 0 and 1 (exclusive)")
    }

    private var layoutDirection = ViewCompat.LAYOUT_DIRECTION_LTR

    // is true if user is swiping actively
    private var touchListening = false

    // when user releases touch it will be true to animate swiped view to its normal place
    private var needSwipeBack = false

    // is true if last touch event is "action_up" or "action_cancel"
    private var touchEnded = false

    // if true must fetch new swipe list
    private var fetchNewList = true

    // current swipe list
    private var mSwipeList: SwipeList? = null

    // if move is enough to be accepted swipe, acception will be current viewHolder as MultiSwipe
    // to call onSwipeDone on it when touch ends otherwise it is null
    private var acception: MultiSwipe? = null

    // used to prevent multi call of `listener.swiping` with same value
    private var lastSwiping = SwipeDirection.NONE

    //Attached recyclerView used to listen touch events of it
    private var recycler: RecyclerView? = null

    // is true when swipe is done and currently swipe is in swipe back mode
    private var acceptBack = false

    private fun listenToRecycler(recyclerView: RecyclerView) {
        touchListening = true
        if(recycler != recyclerView) {
            recyclerView.setOnTouchListener(this)
            recycler = recyclerView
        }
    }


    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = swipeThreshold

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        this.layoutDirection = layoutDirection
        return if (needSwipeBack) {
            needSwipeBack = false
            0
        } else super.convertToAbsoluteDirection(flags, layoutDirection)
    }


    override fun getMovementFlags(recyclerView: RecyclerView,
                                  viewHolder: RecyclerView.ViewHolder): Int =
            makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

    override fun onMove(recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (touchListening){

            if (event.actionMasked == MotionEvent.ACTION_CANCEL
                    || event.actionMasked == MotionEvent.ACTION_UP) {

                touchListening = false
                touchEnded = true
                needSwipeBack = true// can animate swiped button back to its original place

                mSwipeList?.let { list ->
                    // if acception is not null swipe is enough to accept
                    val acceptor = acception
                    if (acceptor != null)
                        list.activeSwipe?.id?.let { id ->
                            acceptBack = true
                            val data = acceptor.onSwipeDone(id)
                            listener?.onSwipeDone(id, data)
                        }
                    else list.shift()// if acception is null switch to next swipe
                }

                acception = null// accepted swipe was notified
            } else if(touchEnded){
                touchEnded = false
                fetchNewList = true// fetch new swipe list
            }
        }
        return false// not consuming touches
    }

    private fun notifySwipe(dX: Float, mid: Float, fromUser: Boolean, size: Int){
        // notify listener about swiping state
        if(listener != null && abs(dX) < mid ){
            val isRtl = layoutDirection == ViewCompat.LAYOUT_DIRECTION_RTL
            val direction = when {
                !fromUser || dX == 0F -> SwipeDirection.NONE
                isRtl xor (dX > 0) -> SwipeDirection.START
                else -> SwipeDirection.END
            }
            if (lastSwiping != direction) {
                lastSwiping = direction
                listener.swiping(direction, size)
            }
        }
    }

    override fun onChildDraw(
            canvas: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float, dY: Float,
            actionState: Int,
            fromUser: Boolean) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, fromUser)
        if (actionState != ItemTouchHelper.ACTION_STATE_SWIPE) return

        if (fromUser) {
            acceptBack = false
            listenToRecycler(recyclerView)
        }

        if (viewHolder !is MultiSwipe)
            throw ClassCastException("ViewHolder must implement com.github.ygngy.multiswipe.MultiSwipe")

        val inverse = supportsRtl && layoutDirection == ViewCompat.LAYOUT_DIRECTION_RTL
        when {
            dX == 0F -> return
            inverse xor (dX < 0) -> {
                if (fetchNewList || mSwipeList !is RightSwipeList)
                    mSwipeList = viewHolder.rightSwipeList ?: RightSwipeList()
            }
            else -> {
                if (fetchNewList || mSwipeList !is LeftSwipeList)
                    mSwipeList = viewHolder.leftSwipeList ?: LeftSwipeList()
            }
        }
        fetchNewList = false


        val swipeList = mSwipeList?.prepare()

        val activeSwipe = swipeList?.activeSwipe

        if (activeSwipe != null){
            
            val mid = viewHolder.itemView.width * getSwipeThreshold(viewHolder)
            val accept = abs(dX) >= mid
            val activeTheme = activeSwipe.getTheme(accept || acceptBack)
            if (fromUser) {
                acception = if (accept) viewHolder else null
            }

            notifySwipe(dX = dX, mid = mid, fromUser = fromUser, size = swipeList.size)

            drawer.apply {
                initDraw(
                        canvas = canvas,
                        recyclerView = recyclerView,
                        viewHolder = viewHolder,
                        dX = dX,
                        swipeList = swipeList,
                        swipe = activeSwipe,
                        swipeTheme = activeTheme,
                        hideInactiveIcons = hideInactiveIcons && (accept || acceptBack),
                        fromUser = fromUser
                )
                drawBackground()
                drawIcons()
                drawLabel()
                drawView()
                drawEnds()
            }

        }
    }
}