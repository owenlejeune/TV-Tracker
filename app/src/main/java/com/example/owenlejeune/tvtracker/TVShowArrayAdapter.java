package com.example.owenlejeune.tvtracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.ArrayList
import java.util.Calendar

/**
 * Created by owenlejeune on 2017-10-30.
 */

class TVShowArrayAdapter(private val context: Context, private val values: ArrayList<TVShow>) : ArrayAdapter<*>(context, -1, values) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.listview_item_row, parent, false)

        val current = values[position]

        val title = rowView.findViewById<TextView>(R.id.show_title_row_label)
        val episode = rowView.findViewById<TextView>(R.id.next_episode_label)
        val date = rowView.findViewById<TextView>(R.id.air_date_label)

        title.text = current.title
        val epString = "s" + current.season + "e" + current.episode
        //episode.setText(epString);

        val c = Calendar.getInstance()
        c.time = current.nextAir

        date.text = epString

        if (c.get(Calendar.YEAR) == 3000) {
            date.text = date.text.toString() + "  ?"
        } else {
            date.text = date.text.toString() + "  " + current.nextAirString
        }

        return rowView
    }
}
