package com.example.evan.leap_3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evan.leap_3.R;
import com.example.evan.leap_3.model.ListItem;

import java.util.List;


/**
 * Created by Evan on 4/20/2017.
 */

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizHolder>
{

    private List<ListItem> listData;
    private LayoutInflater inflater;

    public QuizAdapter(List<ListItem> listData, Context c)
    {
        this.inflater = LayoutInflater.from(c);
        this.listData = listData;
    }


    public void add(ListItem item, int position)
    {
        listData.add(position, item);
        notifyItemInserted(position);

    }

    @Override
    public QuizHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new QuizHolder(view);
    }

    @Override
    public void onBindViewHolder(QuizHolder holder, int position)
    {
        ListItem item = listData.get(position);
        holder.title.setText(item.getQuizName());
        holder.icon.setImageResource(item.getImageResId());
    }

    @Override
    public int getItemCount()
    {
        return listData.size();
    }

    class QuizHolder extends RecyclerView.ViewHolder
    {
        private TextView title;
        private ImageView icon;
        private View container;


        public QuizHolder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.lbl_item_text);
            icon =(ImageView)itemView.findViewById(R.id.im_item_icon);
            container = itemView.findViewById(R.id.cont_item_root);

        }
    }



}
