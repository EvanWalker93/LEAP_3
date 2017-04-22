package com.example.evan.leap_3.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 4/21/2017.
 */

public class QuizData
{
    private static final String[] titles = {"Title1", "Title2", "Title3"};
    private static final int[] icons = {android.R.drawable.ic_popup_reminder, android.R.drawable.ic_menu_add, android.R.drawable.ic_menu_delete};


    public static List<ListItem> getListData(){
        List<ListItem> data = new ArrayList<>();



            for(int i = 0; i <titles.length && i < icons.length; i++)
            {
                ListItem item = new ListItem();
                item.setImageResId(icons[i]);
                item.setTitle(titles[i]);
                data.add(item);

            }


        return data;
    }


}
