package com.questionpro.bullscows.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultAdapter extends BaseAdapter {
    private ArrayList<PassAttempt> passes = new ArrayList<>();
    private LayoutInflater inflater;
    public ResultAdapter(Context context){
       inflater=  LayoutInflater.from(context);
    }
    public void addPassAttempt(PassAttempt passAttempt){
        this.passes.add(0,passAttempt);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return passes.size();
    }

    @Override
    public Object getItem(int position) {
        return passes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return passes.get(position).index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.attempt_list_item, null);
        PassAttempt passAttempt = passes.get(position);
        TextView passIndex = view.findViewById(R.id.index);
        passIndex.setText((passAttempt.index+1)+"");
        TextView bullsCount = view.findViewById(R.id.bullsCount);
        bullsCount.setText(passAttempt.bullsCount+"");
        TextView cowsCount = view.findViewById(R.id.cowsCount);
        cowsCount.setText(passAttempt.cowsCount+"");
        return view;
    }
}
