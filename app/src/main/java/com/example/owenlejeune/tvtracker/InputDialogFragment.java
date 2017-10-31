package com.example.owenlejeune.tvtracker

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast

import java.util.Date

/**
 * Created by owenlejeune on 2017-10-30.
 */

class InputDialogFragment : DialogFragment() {
    var seasonInput: EditText? = null
        private set
    var episodeInput: EditText? = null
        private set
    var titleInput: EditText? = null
        private set
    var airDate: DatePicker? = null
        private set
    private var unknownDateBox: CheckBox? = null
    private var view: View? = null
    var season: Int = 0
        private set
    var episode: Int = 0
        private set
    var title: String? = null
        private set
    var date: Date? = null
        private set
    private var isNew: Boolean = false
    var isHasDate: Boolean = false
        private set
    private var show: TVShow? = null

    override fun onCreateDialog(savedInstaceState: Bundle): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater

        view = inflater.inflate(R.layout.input_layout, null)

        builder.setView(view)

        seasonInput = view!!.findViewById(R.id.season_number)
        episodeInput = view!!.findViewById(R.id.episode_number)
        titleInput = view!!.findViewById(R.id.show_title)
        airDate = view!!.findViewById(R.id.show_calendar)
        unknownDateBox = view!!.findViewById(R.id.unknown_date_check)
        unknownDateBox!!.setOnClickListener {
            if (unknownDateBox!!.isChecked) {
                airDate!!.isEnabled = false
                isHasDate = false
            } else {
                airDate!!.isEnabled = true
                isHasDate = true
            }
        }

        isNew = true
        isHasDate = true

        if (show != null) {
            seasonInput!!.setText("" + show!!.season)
            titleInput!!.setText(show!!.title)
            episodeInput!!.setText("" + show!!.episode)
            val cal = Calendar.getInstance()
            cal.time = show!!.nextAir
            airDate!!.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), null)
        }

        builder.setNegativeButton("CANCEL", null)
        builder.setPositiveButton("CREATE") { dialogInterface, i ->
            season = if (seasonInput!!.text.toString().isEmpty()) 0 else Integer.parseInt(seasonInput!!.text.toString())
            episode = if (episodeInput!!.text.toString().isEmpty()) 0 else Integer.parseInt(episodeInput!!.text.toString())
            title = titleInput!!.text.toString()
            date = getDateFromDatePicker(airDate)

            if (season > 0 && episode > 0 && !title!!.isEmpty()) {
                if (isHasDate || date != null) {
                    listener!!.onDialogPositiveClick(this@InputDialogFragment)
                }
            }
        }

        return builder.create()
    }

    fun setFields(show: TVShow) {
        isNew = false
        //        seasonInput.setText("" + show.getSeason());
        //        titleInput.setText(show.getTitle());
        //        episodeInput.setText("" + show.getEpisode());
        //        Date date = show.getNextAir();
        //        airDate.init(date.getYear(), date.getMonth(), date.getDay(), null);
        this.show = show
    }

    private fun getDateFromDatePicker(picker: DatePicker?): Date {
        val day = picker!!.dayOfMonth
        val month = picker.month
        val year = picker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        return calendar.time
    }


    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            listener = activity as InputDialogListenerInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement InputDialogListenerInterface")
        }

    }

    companion object {

        private var listener: InputDialogListenerInterface? = null
    }
}
