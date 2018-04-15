package com.example.owenlejeune.tvtracker


import android.app.Activity
import android.widget.Toast

import java.io.DataInputStream
import java.io.DataOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by owenlejeune on 2017-10-30.
 */

class TVShow(val title: String, val season: Int, episode: Int, nextAir: Date) {
    var episode: Int = 0
        private set
    var nextAir: Date? = null
        private set
    private val SHOW_INCREMENT = 7
    private val dateFormat = SimpleDateFormat("MMM dd", Locale.CANADA)

    val nextAirString: String
        get() = dateFormat.format(nextAir)

    init {
        this.episode = episode
        this.nextAir = nextAir
    }

    fun nextEpisode() {
        episode++
        val calendar = Calendar.getInstance()
        calendar.time = nextAir
        calendar.add(Calendar.DAY_OF_YEAR, SHOW_INCREMENT)
        nextAir = calendar.time
    }

    fun compareShow(other: TVShow): Int {
        return if (this.nextAir!!.before(other.nextAir)) {
            -1
        } else if (this.nextAir!!.after(other.nextAir)) {
            1
        } else {
            this.title.compareTo(other.title)
        }
    }

    fun writeTo(file: DataOutputStream) {
        try {
            file.writeUTF(title)
            file.writeInt(season)
            file.writeInt(episode)
            file.writeUTF(SDF.format(nextAir))
        } catch (e: Exception) {

        }

    }

    companion object {
        private val SDF = SimpleDateFormat("yyyy-MM-dd", Locale.CANADA)

        fun readFrom(file: DataInputStream): TVShow? {
            try {
                val title = file.readUTF()
                val season = file.readInt()
                val episode = file.readInt()
                val air = SDF.parse(file.readUTF())

                return TVShow(title, season, episode, air)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(null, "Error here", Toast.LENGTH_SHORT).show()
            }

            return null
        }
    }
}
