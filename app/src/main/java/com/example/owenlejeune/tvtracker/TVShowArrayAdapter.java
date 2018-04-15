package com.example.owenlejeune.tvtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by owenlejeune on 2017-10-30.
 */

public class TVShowArrayAdapter extends ArrayAdapter {
    private final Context context;
    private final ArrayList<TVShow> values;

    public TVShowArrayAdapter(Context context, ArrayList<TVShow> values){
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_item_row, parent, false);

        TVShow current = values.get(position);

        TextView title = rowView.findViewById(R.id.show_title_row_label);
        TextView episode = rowView.findViewById(R.id.next_episode_label);
        TextView date = rowView.findViewById(R.id.air_date_label);

        title.setText(current.getTitle());
        String epString = "s" + current.getSeason() + "e" + current.getEpisode();
        //episode.setText(epString);

        Calendar c = Calendar.getInstance();
        c.setTime(current.getNextAir());

        date.setText(epString);

        if(c.get(Calendar.YEAR) == 3000){
            date.setText(date.getText() + "  ?");
        }else{
            date.setText(date.getText() + "  " + current.getNextAirString());
        }

        return rowView;
    }
}
