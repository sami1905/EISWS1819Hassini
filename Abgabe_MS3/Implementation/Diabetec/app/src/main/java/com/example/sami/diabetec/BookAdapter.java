package com.example.sami.diabetec;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private ArrayList<BookItem> mBooklist;

    public static class BookViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewDay;
        public LineChart chart;
        public TextView textViewnValues;
        public TextView textViewAverageValue;
        public TextView textViewTimeInRange;
        public TextView textViewEvents;


        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDay = itemView.findViewById(R.id.text_view_dateOfDay);
            chart = itemView.findViewById(R.id.lineChart_book);
            textViewnValues = itemView.findViewById(R.id.text_view_nValues_book);
            textViewAverageValue = itemView.findViewById(R.id.text_view_averageValue_book);
            textViewTimeInRange = itemView.findViewById(R.id.text_view_timeInRange_book);
            textViewEvents = itemView.findViewById(R.id.text_view_events_book);
        }
    }

    public  BookAdapter(ArrayList<BookItem> bookList){

        mBooklist = bookList;

    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        BookViewHolder evh = new BookViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        BookItem currentItem = mBooklist.get(position);

        holder.textViewDay.setText(currentItem.getDay());
        holder.chart = currentItem.getChart();
        holder.textViewnValues.setText(Integer.toString(currentItem.getnValues()));
        holder.textViewAverageValue.setText(Integer.toString(currentItem.getAverageValue()));
        holder.textViewTimeInRange.setText("unter 55: " + Integer.toString(currentItem.getVeryLowValuesPercents()) + "\n" +
                "unter 80: " + Integer.toString(currentItem.getLowValuesPercents()) + "\n" +
                "80-180: " + Integer.toString(currentItem.getWithinRangePercents()) + "\n" +
                "über 180: " + Integer.toString(currentItem.getHighValuesPercents()) + "\n" );
        holder.textViewEvents.setText(currentItem.getEventList());


    }

    @Override
    public int getItemCount() {
        return mBooklist.size();
    }
}
