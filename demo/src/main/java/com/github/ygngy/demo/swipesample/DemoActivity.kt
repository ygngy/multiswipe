/**
 * Copyright (C) 2021 "Mohamadreza Amani Yeganegi"
 * Licensed under the MIT license
 *
 * Email: ygnegy@gmail.com
 * Github: https://github.com/ygngy
 */

package com.github.ygngy.demo.swipesample

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ygngy.multiswipe.MultiSwipeListener
import com.github.ygngy.multiswipe.SwipeDirection
import com.github.ygngy.multiswipe.multiSwiping
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.random.Random

/**
 * Demo Activity used to show MultiSwipe library in action.
 */
class DemoActivity : AppCompatActivity() {

    companion object{
        private const val EXTRA_DETAIL_ACTIVITY = "com.github.ygngy.demo.swipesample.DETAIL_ACTIVITY"
    }

    private var isDetailActivity = false
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemsList: MutableList<ListItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        fab = findViewById(R.id.addFab)
        isDetailActivity = intent.getBooleanExtra(EXTRA_DETAIL_ACTIVITY, false)
        // having two sample lists each activity uses one of them
        itemsList = (if (isDetailActivity) demoDetailList else demoList).toMutableList()

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(this@DemoActivity)
            addItemDecoration(DividerItemDecoration(this@DemoActivity, DividerItemDecoration.VERTICAL))
            adapter = RecyclerDemoAdapter(itemsList) {
                // starting another sample activity to compare swipe buttons order with current order
                startActivity(
                        Intent(this@DemoActivity,
                                DemoActivity::class.java).apply {
                            putExtra(EXTRA_DETAIL_ACTIVITY, !isDetailActivity)
                        }
                )
            }
        }

        recyclerView.multiSwiping(listener = object : MultiSwipeListener {

            override fun swiping(direction: SwipeDirection, swipeListSize: Int) {
                // here i hide FAB when user is swiping actively
                if (direction == SwipeDirection.END) fab.hide() else fab.show()
            }

            override fun onSwipeDone(swipeId: String, data: Any?) {
                // holder has reacted to swipeDone event and has returned data
                // to this method. I returned viewHolder itself from onSwipeDone at viewHolder
                val holder = data as RecyclerDemoAdapter.ViewHolder
                when (swipeId) {
                    SwipeCreator.SWIPE_TO_SHARE_ID -> shareItem(holder)
                    SwipeCreator.SWIPE_TO_COPY_ID -> copyItem(holder)
                    SwipeCreator.SWIPE_TO_CUT_ID -> cutItem(holder)
                    SwipeCreator.SWIPE_TO_LIKE_ID -> toggleLike(holder)
                    SwipeCreator.SWIPE_TO_EDIT_ID -> editItem(holder)
                    SwipeCreator.SWIPE_TO_DEL_ID -> deleteItem(holder)
                }
            }

        })
    }

    fun onAddClick(view: View){
        val list = if (isDetailActivity) demoDetailList else demoList
        val item = list[Random.nextInt(list.size)]
        itemsList.add(item.copy(id = list.size + 1))
        recyclerView.adapter?.notifyItemInserted(itemsList.lastIndex)
    }

    private fun deleteItem(holder: RecyclerDemoAdapter.ViewHolder){
        itemsList.removeAt(holder.adapterPosition)
        recyclerView.adapter?.notifyItemRemoved(holder.adapterPosition)
    }

    private fun toggleLike(holder: RecyclerDemoAdapter.ViewHolder) {
        holder.item?.toggleLike()
        recyclerView.adapter?.notifyItemChanged(holder.adapterPosition)
    }

    private fun shareItem(holder: RecyclerDemoAdapter.ViewHolder){
        val shareText = holder.item?.data
        if (!shareText.isNullOrBlank()) {
            ShareCompat.IntentBuilder.from(this)
                    .setText(shareText)
                    .setType("text/plain")
                    .setChooserTitle(R.string.share_title)
                    .startChooser()
        }
    }

    private fun copyItem(holder: RecyclerDemoAdapter.ViewHolder){
        val text = holder.item?.data
        if (!text.isNullOrBlank()) {
            val clipData = ClipData.newPlainText("flowers", text)
            (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?)?.setPrimaryClip(clipData)
        }
        Toast.makeText(this, R.string.copy_message, Toast.LENGTH_LONG).show()
    }

    private fun cutItem(holder: RecyclerDemoAdapter.ViewHolder){
        copyItem(holder)
        deleteItem(holder)
    }

    private fun editItem(holder: RecyclerDemoAdapter.ViewHolder){
        val edited = " (edited)"
        holder.item?.let { item ->
            //simply assume item is edited
            item.data += edited
            recyclerView.adapter?.notifyItemChanged(holder.adapterPosition)
        }
    }
}