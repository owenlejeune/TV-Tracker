package com.example.owenlejeune.tvtracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity implements InputDialogListenerInterface {

    private TreeSet<TVShow> showList;
    private String dataFile = "cache.dat";
    private ListView tvshowList;
    private TVShowArrayAdapter listAdapter;
    private Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final InputDialogFragment dialogFragment = new InputDialogFragment();
                dialogFragment.show(getFragmentManager(), "New TV Show");
            }
        });

        showList = new TreeSet<>(new Comparator<TVShow>() {
            @Override
            public int compare(TVShow tvShow, TVShow t1) {
                return tvShow.compareShow(t1);
            }
        });

        tvshowList = findViewById(R.id.main_list);
        tvshowList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TVShow show = (TVShow)adapterView.getItemAtPosition(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setPositiveButton("NEXT EPISODE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TVShow temp = show;
                        showList.remove(show);
                        show.nextEpisode();
                        showList.add(show);
                        update();
                    }
                });
                builder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showList.remove(show);
                        update();
                    }
                });
                builder.setNeutralButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        InputDialogFragment dialog = new InputDialogFragment();
                        dialog.show(getFragmentManager(), "New TV Show");
                        dialog.setFields(show);
                        showList.remove(show);
                        update();
                    }
                });

                builder.show();
            }
        });
        thisActivity = this;
//
//        Calendar c = Calendar.getInstance();
//        c.set(2017, 11, 29);
//
//        for(int i = 0; i < 20; i++){
//            showList.add(new TVShow("show" + i, i, i, c.getTime()));
//            c.add(c.DAY_OF_YEAR, -16);
//        }


        //******************************************//

//        Calendar c = Calendar.getInstance();
//        c.set(2017, 9, 31);
//        showList.add(new TVShow("Brooklyn 99", 5, 5, c.getTime()));
//        c.set(2017, 10, 1);
//        showList.add(new TVShow("South Park", 21, 7, c.getTime()));
//        c.set(2017, 10, 2);
//        showList.add(new TVShow("Modern Family", 9, 6, c.getTime()));
//        showList.add(new TVShow("Orville", 1, 8, c.getTime()));
//        showList.add(new TVShow("Doctor Doctor", 2, 11, c.getTime()));
//        c.set(2017, 9, 29);
//        showList.add(new TVShow("Deuce", 1, 8, c.getTime()));
//        showList.add(new TVShow("Outlander", 3, 7, c.getTime()));
//        showList.add(new TVShow("Bob's Burgers", 8, 4, c.getTime()));
//        showList.add(new TVShow("Family Guy", 16, 5, c.getTime()));
//        c.set(2017, 10, 5);
//        showList.add(new TVShow("Last Week Tonight", 4, 29, c.getTime()));
//        showList.add(new TVShow("Star Trek Discovery", 1, 8, c.getTime()));
//        showList.add(new TVShow("Shameless", 8, 1, c.getTime()));
//        c.set(3000, 0, 1);
//        showList.add(new TVShow("Comrade Detective", 2, 1, c.getTime()));
//        showList.add(new TVShow("Grand Tour", 2, 1, c.getTime()));
//        showList.add(new TVShow("Sherlock", 5, 1, c.getTime()));
//        showList.add(new TVShow("Silicon Valley", 5, 1, c.getTime()));
//        showList.add(new TVShow("Game of Thrones", 8, 1, c.getTime()));
//        showList.add(new TVShow("Insecure", 3, 1, c.getTime()));
//        showList.add(new TVShow("Rick and Morty", 4, 1, c.getTime()));


        //*****************************************//
        read(dataFile, showList);

        update();

    }

    public void update(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<TVShow> temp = new ArrayList<>();
                temp.addAll(showList);
                listAdapter = new TVShowArrayAdapter(thisActivity.getApplicationContext(), temp);
                tvshowList.setAdapter(listAdapter);
                write(dataFile, temp);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onDialogPositiveClick(InputDialogFragment fragment){
        int season = fragment.getSeason();
        int episode = fragment.getEpisode();
        String title = fragment.getTitle();

        Date date;

        if(fragment.isHasDate()){
            date = fragment.getDate();
        }else{
            Calendar c = Calendar.getInstance();
            c.set(3000, 0, 1);
            date = c.getTime();
        }

       // Date date = fragment.getDate();
//        TVShow temp = new TVShow(title, season, episode, date);
        showList.add(new TVShow(title, season, episode, date));
        update();
    }

    public void write(String file, ArrayList<TVShow> data){
        try{
            FileOutputStream out = openFileOutput(file, Context.MODE_PRIVATE);
            DataOutputStream dataOut = new DataOutputStream(out);

            for(TVShow t : data){
                t.writeTo(dataOut);
            }
            //Toast.makeText(getApplicationContext(), "data written to " + file, Toast.LENGTH_SHORT).show();
            dataOut.close();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(thisActivity, "No file found", Toast.LENGTH_SHORT).show();
        }
    }

    public void read(String file, TreeSet<TVShow> data){
        try{
            FileInputStream in = openFileInput(file);
            DataInputStream dataIn = new DataInputStream(in);

            while(dataIn.available() > 0){
//                TVShow s = TVShow.readFrom(dataIn);
                data.add(TVShow.Companion.readFrom(dataIn));
//                data.add(s);
            }
            dataIn.close();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(thisActivity, "No file found ", Toast.LENGTH_SHORT).show();
        }
    }
}
