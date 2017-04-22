package com.example.evan.leap_3.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evan.leap_3.R;

import com.example.evan.leap_3.model.QuizItem;

import java.util.List;

/**
 * Created by Evan on 4/22/2017.
 */

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.MyViewHolder>
{
    private List<QuizItem> quizList;




    public class MyViewHolder extends  RecyclerView.ViewHolder
    {
        public TextView quizName, quizPath;


        public MyViewHolder(View view)
        {
            super(view);
            quizName = (TextView) view.findViewById(R.id.quizPath);
            quizPath = (TextView) view.findViewById(R.id.quizName);
        }
    }

    public QuizAdapter(List<QuizItem> quizList)
    {
        this.quizList = quizList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        QuizItem quiz = quizList.get(position);
        holder.quizPath.setText(quiz.getQuizFilePath());
        holder.quizName.setText(quiz.getQuizFileName());
    }

    @Override
    public int getItemCount()
    {
        return quizList.size();
    }
}
