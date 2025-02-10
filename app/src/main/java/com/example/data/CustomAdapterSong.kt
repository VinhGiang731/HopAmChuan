package com.example.data

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.hopamchuan.ItemClick
import com.example.hopamchuan.R

class CustomAdapterSong(
    private val list: MutableList<Song>,
    private val activity: Activity,
    private val onItem: ItemClick
) : ArrayAdapter<Song>(activity, R.layout.custom_item_song) {
    override fun getCount(): Int {
        return list.size
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = convertView ?: activity.layoutInflater.inflate(R.layout.custom_item_song, parent, false)

        val songTitle = rowView.findViewById<TextView>(R.id.txt_title)
        val authorName = rowView.findViewById<TextView>(R.id.txt_author)
        val chordList = rowView.findViewById<TextView>(R.id.txt_chords)

        songTitle.text = list[position].songTitle
        authorName.text = list[position].authorName
        chordList.text = list[position].chordList

        rowView.setOnClickListener {
            onItem.onClickItem(position)
        }

        rowView.setOnLongClickListener {
            onItem.onLongItem(position)
            true
        }
        return rowView
    }
}