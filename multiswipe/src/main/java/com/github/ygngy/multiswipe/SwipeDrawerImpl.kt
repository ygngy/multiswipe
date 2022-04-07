/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegi"
 * Licensed under the MIT license
 *
 * Email: ygnegy@gmail.com
 * Github: https://github.com/ygngy
 */

package com.github.ygngy.multiswipe

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Default class for drawing backgrounds, icons and labels in swipes.
 * Could be extended to change drawing of icons, labels and backgrounds in swipes.
 */
open class SwipeDrawerImpl: SwipeDrawer {

    // Paint used to draw swipe background
    private val bkgPaint = Paint().apply {
        style = Paint.Style.FILL
    }

    // TextPaint used to draw swipe labels
    private val txtPaint = TextPaint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.LEFT
    }

    // Used to calculate text height
    private val textBounds = Rect()

    private lateinit var canvas: Canvas
    private lateinit var swipeList: SwipeList
    private lateinit var swipeTheme: SwipeTheme
    private var hideInactiveIcons = true
    private var dX: Float = 0F
    private var rowLeft: Float = 0F
    private var rowRight: Float = 0F
    private var rowTop: Float = 0F
    private var rowBottom: Float = 0F
    private var xPos: Float = 0F
    private var visionHeight: Int = 0
    private var yCenter: Float = 0F

    /**
     * @return true if [pos] is hidden below swiped view.
     *
     * [pos] is horizontal position
     */
    private fun isPositionHidden(pos: Float): Boolean = when {
        dX > 0 -> rowLeft + dX < pos
        dX < 0 -> rowRight + dX > pos
        else -> true// if dX == 0 then pos is always hidden
    }

    override fun initDraw(canvas: Canvas,
                      recyclerView: RecyclerView,
                      viewHolder: RecyclerView.ViewHolder,
                      dX: Float,
                      swipeList: SwipeList, swipe: Swipe,
                      swipeTheme: SwipeTheme,
                      hideInactiveIcons: Boolean,
                      fromUser: Boolean) {
        this.canvas = canvas
        this.swipeList = swipeList
        this.swipeTheme = swipeTheme
        this.hideInactiveIcons = hideInactiveIcons
        this.dX = dX
        this.rowLeft = viewHolder.itemView.left.toFloat()
        this.rowRight = viewHolder.itemView.right.toFloat()
        this.rowTop = viewHolder.itemView.top.toFloat()
        this.rowBottom = viewHolder.itemView.bottom.toFloat()
        this.xPos = (if (dX < 0) viewHolder.itemView.right else viewHolder.itemView.left).toFloat()
        val visionTop = max(viewHolder.itemView.top, 0)
        val visionBottom = min(viewHolder.itemView.bottom, recyclerView.height)
        this.visionHeight = abs(visionBottom - visionTop)
        this.yCenter = (visionTop + visionBottom) / 2f
    }

    override fun drawBackground() {
        bkgPaint.color = swipeTheme.backgroundColor
        if (bkgPaint.color != Color.TRANSPARENT) when {
            dX > 0 -> canvas.drawRect(rowLeft, rowTop, rowLeft + dX, rowBottom, bkgPaint)
            dX < 0 -> canvas.drawRect(rowRight + dX, rowTop, rowRight, rowBottom, bkgPaint)
        }
    }

    override fun drawView(){
        bkgPaint.color = swipeTheme.viewColor
        // drawing a background below view to hide text and icons below view
        if (bkgPaint.color != Color.TRANSPARENT) when {
            dX >= 0 -> canvas.drawRect(rowLeft + dX, rowTop, rowRight, rowBottom, bkgPaint)
            else -> canvas.drawRect(rowLeft, rowTop, rowRight + dX, rowBottom, bkgPaint)
        }
    }

    override fun drawLabel() {
        val swipeLabel = swipeTheme.label
        if (swipeLabel != null && swipeLabel.text.isNotBlank()
                && swipeLabel.textColor != Color.TRANSPARENT
                && swipeLabel.textSize > 0) {
            var txtXOrigin = xPos

            when {
                dX > 0 -> {
                    txtPaint.textAlign = Paint.Align.LEFT
                    txtXOrigin += swipeLabel.margin
                }
                dX < 0 -> {
                    txtPaint.textAlign = Paint.Align.RIGHT
                    txtXOrigin -= swipeLabel.margin
                }
            }

            // If label position is hidden behind view, there is no need to draw label
            if (isPositionHidden(txtXOrigin)) return

            txtPaint.apply {
                color = swipeLabel.textColor
                textSize = swipeLabel.textSize
                getTextBounds(swipeLabel.text, 0, swipeLabel.text.length, textBounds)
            }
            // only drawing text if there is enough height space for it
            if (textBounds.height() <= visionHeight) {
                val txtYOrigin = yCenter + (textBounds.height() / 2f) - textBounds.bottom
                canvas.drawText(swipeLabel.text, txtXOrigin, txtYOrigin, txtPaint)
            }
        }
    }


    override fun drawIcons(){

        if (abs(dX) < swipeTheme.icon.edgeHorMargin) return

        var iconLeft = xPos

        for (swipe in swipeList) {

            if (hideInactiveIcons && swipeList.position > 0 ) break

            val icon = if (swipeList.position == 0) swipeTheme.icon.drawable
            else swipe.inactiveIcon


            val iconWidth: Int
            val iconHeight: Int

            if (icon.intrinsicHeight <= visionHeight) {
                iconHeight = icon.intrinsicHeight
                iconWidth = icon.intrinsicWidth
            } else {
                // when view's height is smaller than icon's height or user scrolls
                // part of view out of view point and vision's height is smaller than icon's height
                // then i draw smaller icons
                iconHeight = visionHeight
                iconWidth = ((visionHeight.toFloat()/icon.intrinsicHeight.toFloat()) * icon.intrinsicWidth).toInt()
            }

            // Here i always draw icon vertically in middle of visible part of row instead of middle of row
            // because middle of row may not be visible ( if user scrolls part of row out of view)
            val iconTop = (yCenter - (iconHeight / 2f)).toInt()

            val horMargin = swipeTheme.getHorizontalMargin(swipeList.position)

            if (dX < 0) iconLeft -= horMargin else iconLeft += horMargin
            if (isPositionHidden(iconLeft)) break

            if (dX < 0) iconLeft -= iconWidth

            icon.setBounds(
                    iconLeft.toInt(),
                    iconTop,
                    (iconLeft + iconWidth).toInt(),
                    iconTop + iconHeight
            )
            icon.draw(canvas)

            if (dX >= 0) iconLeft += iconWidth

            xPos =  iconLeft// setting xPos for label's xOrigin
            // xPos will be used at drawLabel method after this method
        }
    }

    override fun drawEnds() {
    }

}