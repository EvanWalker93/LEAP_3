package com.example.evan.leap.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.evan.leap.R;
import com.example.evan.leap.adapter.QuizAdapter;
import com.example.evan.leap.model.QuizItem;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;



public class MainActivity extends AppCompatActivity {

    private Button mBrowseButton;
    private ArrayList<QuizItem> quizList = new ArrayList<>();
    private RecyclerView recView;
    private QuizAdapter adapter;
    private String fileName = "test.txt";
    private static final String EXTRA_QUIZ_ITEMS = "quiz_items";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (ContextCompat.checkSelfPermission(MainActivity.this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // permission not granted, requesting it:
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{READ_EXTERNAL_STORAGE}, 0);
        } else {
            // permission already granted, do something
        }


        //Opens a file containing items to populate the list
        //readFileToView();


        //Toast.makeText(MainActivity.this, "Loaded", Toast.LENGTH_LONG).show();


        //Button to open file browser
        mBrowseButton = (Button) findViewById(R.id.browse_button);
        mBrowseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseForFiles();
            }
        });

        //Creates the RecyclerView to display quiz items, and sets up the layout
        recView = (RecyclerView) findViewById(R.id.quizRecyclerView);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setItemAnimator(new DefaultItemAnimator());

        //Creates the adapter and sets the RecyclerView's adapter to it
        adapter = new QuizAdapter(quizList);
        recView.setAdapter(adapter);

        //Dummy item to test the adapter + RecView
        QuizItem item = new QuizItem("Test Path", "Test Name");
        quizList.add(item);


        //String quizPath = readFile(fileName).toString();
        //String quizName = (quizPath.substring(quizPath.lastIndexOf("/") + 1));
        //QuizItem testItem = new QuizItem(quizPath, quizName);
        //quizList.add(testItem);


        if (savedInstanceState != null) {
            Toast.makeText(MainActivity.this, "Loaded savedInstanceState", Toast.LENGTH_LONG).show();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            Set<String> QuizItems = preferences.getStringSet(EXTRA_QUIZ_ITEMS, new HashSet<String>());
            List<String> list = new ArrayList<>();

            System.out.println("SAVED INSTANCE RESULT "+preferences.getStringSet(EXTRA_QUIZ_ITEMS, new HashSet<String>()));
            //quizList.add((QuizItem) list);

        }

    }

    @Override
    public void onPause() {
        SharedPreferences sharedPref = getSharedPreferences(EXTRA_QUIZ_ITEMS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> set = new HashSet<>();

        List<String> paths = new ArrayList<>();
        for (QuizItem item : quizList) {
            paths.add(item.getQuizFilePath());
            System.out.println(item.getQuizFilePath());
        }
        set.addAll(paths);
        editor.putStringSet(EXTRA_QUIZ_ITEMS, set);
        editor.apply();
        System.out.println("OnPause written");
        super.onPause();


    }

    private void browseForFiles() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(1)
                .withHiddenFiles(true) // Show hidden files and folders
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            String fileName = (filePath.substring(filePath.lastIndexOf("/") + 1) + " ");

            //Adds the selected item to the quizList view and writes the path to file
            QuizItem item = new QuizItem(filePath, fileName);
            quizList.add(item);
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Added " + fileName, Toast.LENGTH_LONG).show();



        }
    }




    @Override
    public void onStop() {
        super.onStop();
    }





}



