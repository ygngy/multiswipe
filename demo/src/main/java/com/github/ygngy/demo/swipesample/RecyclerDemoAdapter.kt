/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegi"
 * Licensed under the MIT license
 *
 * Email: ygnegy@gmail.com
 * Github: https://github.com/ygngy
 */

package com.github.ygngy.demo.swipesample


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.ygngy.multiswipe.*


class RecyclerDemoAdapter(private val dataSet: List<ListItem>
        , private val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<RecyclerDemoAdapter.ViewHolder>(){

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view), MultiSwipe {
        private val context = view.context
        private val textView: TextView = view.findViewById(R.id.title)
        private val imageView: ImageView = view.findViewById(R.id.like)
        // Hold left and right swipe lists and reuse them
        // Prevent recreating them when there is no need to create it
        // Only recreate swipe lists when you need to change a swipe
        private var mLeftSwipeList: LeftSwipeList? = null
        private var mRightSwipeList: RightSwipeList? = null
        var item: ListItem? = null
            private set

        fun bind(listItem: ListItem) {
            this.item = listItem
            imageView.visibility = if (listItem.liked) View.VISIBLE else View.GONE
            textView.text = listItem.data
            view.setOnClickListener {
                onClick(listItem.id)
            }

            val sc = SwipeCreator(context, listItem.liked)
            // Testing different swipe counts based on data
            when(listItem.data){
                TWO_LEFT_SWIPE_ONE_RIGHT_SWIPE -> {
                    mLeftSwipeList = LeftSwipeList(sc.shareSwipe, sc.cutSwipe)
                    mRightSwipeList = RightSwipeList(sc.likeSwipe)
                }
                ONE_LEFT_SWIPE_TWO_RIGHT_SWIPE -> {
                    mLeftSwipeList = LeftSwipeList(sc.shareSwipe)
                    mRightSwipeList = RightSwipeList(sc.likeSwipe, sc.delSwipe)
                }
                NO_LEFT_SWIPE -> {
                    mLeftSwipeList = null
                    mRightSwipeList = RightSwipeList(
                            sc.likeSwipe, sc.editSwipe, sc.delSwipe
                    )
                }
                NO_RIGHT_SWIPE -> {
                    mLeftSwipeList = LeftSwipeList(
                            sc.shareSwipe, sc.copySwipe, sc.cutSwipe
                    )
                    mRightSwipeList = null
                }
                else -> {
                    mLeftSwipeList = LeftSwipeList(
                            sc.shareSwipe, sc.copySwipe, sc.cutSwipe
                    )
                    mRightSwipeList = RightSwipeList(
                            sc.likeSwipe, sc.editSwipe, sc.delSwipe
                    )
                }
            }
        }

        override val leftSwipeList: LeftSwipeList?
            get() = mLeftSwipeList

        override val rightSwipeList: RightSwipeList?
            get() = mRightSwipeList

        // Here i can react to swipe click and return some
        // data to main MultiSwipeListener (if there is one)
        // for this demo i simply return this viewHolder
        // to MultiSwipeListener
        override fun onSwipeDone(swipeId: String): Any? = this
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bind(dataSet[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


}
