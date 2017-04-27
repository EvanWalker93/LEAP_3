package com.example.evan.leap.ui;


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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.evan.leap.R;
import com.example.evan.leap.adapter.QuizAdapter;
import com.example.evan.leap.model.QuizItem;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

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
    private static final String EXTRA_QUIZ_ITEMS = "quiz_items";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(), "on Create", Toast.LENGTH_SHORT).show();


        if (ContextCompat.checkSelfPermission(MainActivity.this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // permission not granted, requesting it:
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{READ_EXTERNAL_STORAGE}, 0);
        } else {
            // permission already granted, do something
        }



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

        loadItems();
        if (savedInstanceState != null) {
            loadItems();
            adapter.notifyDataSetChanged();
        }

    }


    public void loadItems(){

        Toast.makeText(MainActivity.this, "Loaded Items", Toast.LENGTH_SHORT).show();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> QuizItems = preferences.getStringSet(EXTRA_QUIZ_ITEMS, new HashSet<String>());

        String path = QuizItems.toString().replace("[","").replace("]","").trim();
        String[] splitPaths = path.split(",");


        quizList.clear();
        for (int i=0; i < splitPaths.length; i++){
            path = splitPaths[i].toString();
            if(path == ""){break;}
            String name = (path.substring(path.lastIndexOf("/") + 1) + " ");
            QuizItem item = new QuizItem(path, name);
            quizList.add(item);
        }

    }

    public void saveItems(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> set = new HashSet<>();

        List<String> paths = new ArrayList<>();
        for (QuizItem item : quizList) {
            paths.add(item.getQuizFileName());
        }

        set.addAll(paths);
        editor.putStringSet(EXTRA_QUIZ_ITEMS, set);
        editor.apply();

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

            //Loads items after adding, to avoid duplicate entries in the view
            loadItems();
            Toast.makeText(getApplicationContext(), "Added " + fileName, Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public void onPause() {
        saveItems();
        super.onPause();
    }

    @Override
    public void onStop() {
        saveItems();
        super.onStop();
    }




}



