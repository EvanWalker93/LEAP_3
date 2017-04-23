package com.example.evan.leap.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
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
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;



public class MainActivity extends AppCompatActivity {

    private Button mBrowseButton;
    private List<QuizItem> quizList = new ArrayList<>();
    private RecyclerView recView;
    private QuizAdapter adapter;
    private String fileName = "test.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // permission not granted, requesting it:
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{READ_EXTERNAL_STORAGE}, 0);
        } else {
            // permission already granted, do something
        }


        //Opens a file containing items to populate the list
        //readFileToView();


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
        QuizItem item = new QuizItem("Test String", "Test Sub-string");
        quizList.add(item);


        String quizPath = readFile(fileName).toString();
        String quizName = (quizPath.substring(quizPath.lastIndexOf("/") + 1));
        QuizItem testItem = new QuizItem(quizPath, quizName);
        quizList.add(testItem);

    }




    private void browseForFiles() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(1)
                .withHiddenFiles(true) // Show hidden files and folders
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            String fileName = (filePath.substring(filePath.lastIndexOf("/") + 1) + " ");

            Log.d("MainActivity", "File path: " + filePath);
            Log.d("MainActivity", "File Selected");

            //Adds the selected item to the quizList view and writes the path to file
            QuizItem item = new QuizItem(filePath, fileName);
            quizList.add(item);
            writeFile(fileName, filePath);
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Added " + fileName, Toast.LENGTH_LONG).show();

            //TODO: Create File from file path
            //TODO: Read in file (FileReader + BufferedReader) as String[]
            //TODO: RecyclerView (or ListView)
        }
    }

    public void writeFile(String file, String text)
    {
        try
        {
            FileOutputStream fos = openFileOutput(file, Context.MODE_PRIVATE);
            fos.write(text.getBytes());
            fos.close();
            Toast.makeText(MainActivity.this, "File Saved", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Error Saving File", Toast.LENGTH_LONG).show();
        }
    }

    public String readFile(String file)
    {
        String text = "";

        try
        {
            FileInputStream fis = openFileInput(file);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            text = new String(buffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "File Not Found", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Error Loading File", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return text;
    }
}



