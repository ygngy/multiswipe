package com.github.ygngy.demo.swipesample


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RecyclerDemoAdapter(private val dataSet: List<ListItem>
        , private val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<RecyclerDemoAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.title)
        private val imageView: ImageView = view.findViewById(R.id.like)
        var item: ListItem? = null
            private set

        fun bind(listItem: ListItem){
            this.item = listItem
            imageView.visibility = if (listItem.liked) View.VISIBLE else View.GONE
            textView.text = listItem.data
            view.setOnClickListener {
                onClick(listItem.id)
            }
        }
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
