package com.example.evan.leap_3.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
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


import com.example.evan.leap_3.R;
import com.example.evan.leap_3.adapter.QuizAdapter;
import com.example.evan.leap_3.model.ListItem;
import com.example.evan.leap_3.model.QuizData;
import com.example.evan.leap_3.model.QuizItem;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private Button mBrowseButton;
    private List<ListItem> quizList = new ArrayList<>();
    private RecyclerView recView;
    private QuizAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            // permission not granted, requesting it:
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{READ_EXTERNAL_STORAGE}, 0);
        }
        else {
            // permission already granted, do something
        }

        //Button to open file browser
        mBrowseButton = (Button) findViewById(R.id.browse_button);
        mBrowseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Hello!");
                browseForFiles();
            }
        });


        recView = (RecyclerView)findViewById(R.id.quizRecyclerView);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setItemAnimator(new DefaultItemAnimator());

        adapter = new QuizAdapter(QuizData.getListData(), this);
        recView.setAdapter(adapter);








    }

    private void browseForFiles()
    {
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
            String fileName = filePath.substring(filePath.lastIndexOf("/")+1);

            Log.d("MainActivity", "File path: " + filePath);
            Log.d("MainActivity", "File Selected");
            QuizItem item = new QuizItem(fileName, filePath);

            quizList.add(item);



            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(),"Added " + fileName, Toast.LENGTH_LONG).show();

            //TODO: Create File from file path
            //TODO: Read in file (FileReader + BufferedReader) as String[]
            //TODO: RecyclerView (or ListView)
        }
    }


}
