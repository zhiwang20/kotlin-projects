package com.example.fetchrewardproject

import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.view.View

class ItemAdapter(private val mContext: Context, var mResource: Int, objects: List<Item?>) :
    ArrayAdapter<Item?>(mContext, mResource, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val id = getItem(position)!!.id
        val listId = getItem(position)!!.listId
        val name = getItem(position)!!.name
        val inflater = LayoutInflater.from(mContext)
        convertView = inflater.inflate(mResource, parent, false)
        val tvid = convertView.findViewById<TextView>(R.id.textView1)
        val tvlistId = convertView.findViewById<TextView>(R.id.textView2)
        val tvName = convertView.findViewById<TextView>(R.id.textView3)
        tvid.text = id
        tvlistId.text = listId
        tvName.text = name
        return convertView
    }
}