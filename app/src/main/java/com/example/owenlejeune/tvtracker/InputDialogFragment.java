package com.example.owenlejeune.tvtracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by owenlejeune on 2017-10-30.
 */

public class InputDialogFragment extends DialogFragment {

    private static InputDialogListenerInterface listener;
    private EditText seasonInput, episodeInput, titleInput;
    private DatePicker airDate;
    private CheckBox unknownDateBox;
    private View view;
    private int season, episode;
    private String title;
    private Date date;
    private boolean isNew;
    private boolean hasDate;
    private TVShow show;

    public Dialog onCreateDialog(Bundle savedInstaceState){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.input_layout, null);

        builder.setView(view);

        seasonInput = view.findViewById(R.id.season_number);
        episodeInput = view.findViewById(R.id.episode_number);
        titleInput = view.findViewById(R.id.show_title);
        airDate = view.findViewById(R.id.show_calendar);
        unknownDateBox = view.findViewById(R.id.unknown_date_check);
        unknownDateBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(unknownDateBox.isChecked()){
                    airDate.setEnabled(false);
                    hasDate = false;
                }else{
                    airDate.setEnabled(true);
                    hasDate = true;
                }
            }
        });

        isNew = true;
        hasDate = true;

        if(show != null){
            seasonInput.setText("" + show.getSeason());
            titleInput.setText(show.getTitle());
            episodeInput.setText("" + show.getEpisode());
            Calendar cal = Calendar.getInstance();
            cal.setTime(show.getNextAir());
            airDate.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), null);
        }

        builder.setNegativeButton("CANCEL", null);
        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                season = (seasonInput.getText().toString().isEmpty()) ? 0 : Integer.parseInt(seasonInput.getText().toString());
                episode = (episodeInput.getText().toString().isEmpty()) ? 0 : Integer.parseInt(episodeInput.getText().toString());
                title = titleInput.getText().toString();
                date = getDateFromDatePicker(airDate);

                if(season > 0 && episode > 0 && !title.isEmpty()){
                    if(hasDate || date != null){
                        listener.onDialogPositiveClick(InputDialogFragment.this);
                    }
                }
            }
        });

        return builder.create();
    }

    public void setFields(TVShow show){
        isNew = false;
//        seasonInput.setText("" + show.getSeason());
//        titleInput.setText(show.getTitle());
//        episodeInput.setText("" + show.getEpisode());
//        Date date = show.getNextAir();
//        airDate.init(date.getYear(), date.getMonth(), date.getDay(), null);
        this.show = show;
    }

    private Date getDateFromDatePicker(DatePicker picker){
        int day = picker.getDayOfMonth();
        int month = picker.getMonth();
        int year = picker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }


    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            listener = (InputDialogListenerInterface) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement InputDialogListenerInterface");
        }
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
        return episode;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public EditText getSeasonInput() {
        return seasonInput;
    }

    public EditText getEpisodeInput() {
        return episodeInput;
    }

    public EditText getTitleInput() {
        return titleInput;
    }

    public DatePicker getAirDate() {
        return airDate;
    }

    public boolean isHasDate() {
        return hasDate;
    }
}
