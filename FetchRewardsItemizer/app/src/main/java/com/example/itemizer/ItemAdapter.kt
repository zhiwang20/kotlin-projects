package com.example.itemizer

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*  //in build.gradle: id 'kotlin-android-extensions'

/*
 * The ItemAdapter class adapts a list of items to the recycler view.
 * RecyclerView takes advantage of the fact that as you scroll and new rows come on screen also old rows disappear off screen.
   Instead of creating new view for each new row, an old view is recycled and reused by binding new data to it.----onBindViewHolder()
 */
class ItemAdapter(private var items: MutableList<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // Methods; used by MainActivity.kt
    fun setItems(items1 : MutableList<Item>) {
        //Log.d("Zhiqiang", items1.toString())  //message needs string so using toString(); or use $
        Log.d("Zhiqiang", "000")   //only call once
        items = items1
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //this only initially show 17 times, then as screen down shows a max of 5 more messages(one time)
        Log.d("wang", "$viewType")   //parent is androidx.recyclerview.widget.RecyclerView{1617eb1 VFED..... .F....ID 0,64-704,64 #7f0800e1 app:id/itemListRecyclerView}
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //when this app opens, this log shows 17(0-16) positions(screen only can have 17 items with header included);
        //as we scroll down the list, position increases(up to 319); same for scroll up the list, the position decreases
        //if we scroll down 5 items, then this logs only show new 5 position-->bottom 5 replaces top 5
        Log.d("zw", "$position")   //holder is ItemViewHolder{9118717 position=0 id=-1, oldPos=-1, pLpos:-1 no parent}
        val curItem = items[position]

        holder.itemView.apply {
            //don't need findViewById; in build.gradle: id 'kotlin-android-extensions'
            idTextView.text = curItem.id.toString()
            listIdTextView.text = curItem.listId.toString()
            nameTextView.text = if (curItem.name != null && curItem.name.isNotEmpty()) curItem.name else "N/A"
        }
    }

    //returns The number of items currently available in adapter; screen only 17 currently
    override fun getItemCount(): Int {
        return items.size
    }
}