package com.example.sami.diabetec;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ExampleViewHolder> {
    private ArrayList<ReportItem> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView1;
        public  TextView mTextView2;

        public ExampleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textView_example);
            mTextView2 = itemView.findViewById(R.id.textView_example2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ReportsAdapter(ArrayList<ReportItem> exampleList){
        mExampleList = exampleList;

    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ReportItem currentItem = mExampleList.get(position);

        holder.mTextView1.setText(currentItem.getTitle());
        holder.mTextView2.setText(currentItem.getDescription());

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
