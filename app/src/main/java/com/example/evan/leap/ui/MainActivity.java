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
import android.support.v7.widget.DividerItemDecoration;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;


public class MainActivity extends AppCompatActivity {

    private static final String EXTRA_QUIZ_ITEMS = "quiz_items";
    private ArrayList<QuizItem> quizList = new ArrayList<>();
    private QuizAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(MainActivity.this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // permission not granted, requesting it:
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{READ_EXTERNAL_STORAGE}, 0);
        }


        //Button to open file browser
        Button mBrowseButton = (Button) findViewById(R.id.browse_button);
        mBrowseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseForFiles();
            }
        });

        //Creates the RecyclerView to display quiz items, and sets up the layout
        RecyclerView recView = (RecyclerView) findViewById(R.id.quizRecyclerView);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setItemAnimator(new DefaultItemAnimator());
        recView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        //Creates the adapter and sets the RecyclerView's adapter to it
        adapter = new QuizAdapter(quizList);
        recView.setAdapter(adapter);
        loadItems();
    }


    public void loadItems() {

        //Loads the list of items that were previously saved
        //to the SharedPreferences to the set of strings QuizItems
        Toast.makeText(MainActivity.this, "Loaded Items", Toast.LENGTH_SHORT).show();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> QuizItems = preferences.getStringSet(EXTRA_QUIZ_ITEMS, new HashSet<String>());

        //If the set QuizItems is empty, assume no paths were saved and stop loading
        if (QuizItems.isEmpty()) {
            Log.d("loadItems: ","No items present in QuizItem set");
            return;
        }

        Log.d("QuizItems", QuizItems.toString());
        //Puts the saved elements into a single string 'path'
        String path = QuizItems.toString();


        //Removes the brackets from the string
        path = path.substring(1, path.length() -1);
        Log.d("Path String", path);



        //Split the string 'path' when there is a comma, then put it into the array 'splitPaths'
        //Then, sort the array alphabetically by the file name, not the file path
        String[] splitPaths = path.split(", ");
        Arrays.sort(splitPaths, new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                String substr1 = str1.substring(str1.lastIndexOf("/"));
                Log.d("Path String", substr1);
                String substr2 = str2.substring(str2.lastIndexOf("/"));
                Log.d("Path String", substr2);
                return substr1.compareTo(substr2);
            }
        });


        quizList.clear();

        //Add all strings in splitPaths to the adapter quizList, then update the view
        for (String splitPath : splitPaths) {
            path = splitPath;
            if (path == null) {
                break;
            }
            String name = (path.substring(path.lastIndexOf("/") + 1));
            QuizItem item = new QuizItem(path, name);
            quizList.add(item);
            adapter.notifyDataSetChanged();
        }

    }

    public void saveItems() {
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
            String fileName = (filePath.substring(filePath.lastIndexOf("/") + 1) + "");

            if (quizList.isEmpty()) {
                quizList.add(new QuizItem(filePath, fileName));
                saveItems();
                loadItems();
                adapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "Quiz List Was Empty", Toast.LENGTH_SHORT).show();
                return;
            } else {
                for (QuizItem item : quizList) {

                    if (Objects.equals(item.getQuizFilePath(), fileName)) {

                        //Adds the selected item to the quizList view and writes the path to file
                        Toast.makeText(getApplicationContext(), "Item Already Added", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }
            }


            quizList.add(new QuizItem(filePath, fileName));
            saveItems();
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
        //saveItems();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        //saveItems();
        super.onDestroy();
    }

}








