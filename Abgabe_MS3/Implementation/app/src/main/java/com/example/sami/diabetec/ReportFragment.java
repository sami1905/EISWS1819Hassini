package com.example.sami.diabetec;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ReportFragment extends Fragment {
    private ArrayList<ExampleItem> exampleList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);



        exampleList.add(new ExampleItem("Durchschnittswerte", "Schau Dir Deine Durchschnittswerte, wie z.B. Deinen HbA1c-Wert, an."));
        exampleList.add(new ExampleItem("Muster", "Schau Dir an, wann Deine Blutzuckerwerte in den letzten 7 Tagen ein Muster aufweisen."));
        exampleList.add(new ExampleItem("Zeit im Zielbereich", "Schau Dir an, wie oft Deine Blutzuckerwerte über, unter und im Zielbereich liegen."));
        exampleList.add(new ExampleItem("Durchschnittstag", "Schau Dir Deinen durchschnittlichen Tag an."));
        exampleList.add(new ExampleItem("Bester Tag", "Schau Dir Deinen besten Tag an."));
        exampleList.add(new ExampleItem("Überlagerung", "Schau Dir alle Graphen der letzten 7 Tage an."));
        exampleList.add(new ExampleItem("Stündliche Statistiken", "Schau Dir Deine stündlichen Statistiken an."));




        mRecyclerView = view.findViewById(R.id.recycler_view_reports);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ExampleAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(position == 0){
                    openAverageValuesActivity();

                }
                else if(position == 1){
                    openMusterActivity();
                }

                else if(position == 2){
                    openTimeWithinRangeActivity();
                }

                else if(position == 3){
                    openAverageDayActivity();

                }

                else if(position == 4){
                    openBestDayActivity();
                }

                else if(position == 5){
                    openOverlayActivity();
                }

                else if(position == 6){
                    openStaticsByHourActivity();

                }
            }
        });

        return view;
    }

    public void openStaticsByHourActivity(){
        Intent intent = new Intent(getContext(), StaticsByHourActivity.class);
        startActivity(intent);
    }

    public void openOverlayActivity(){
        Intent intent = new Intent(getContext(), OverlayActivity.class);
        startActivity(intent);
    }

    public void openAverageDayActivity(){
        Intent intent = new Intent(getContext(), AverageDayActivity.class);
        startActivity(intent);
    }


    public void openTimeWithinRangeActivity(){
        Intent intent = new Intent(getContext(), TimeWithinRangeActivity.class);
        startActivity(intent);
    }

    public void openAverageValuesActivity(){
        Intent intent = new Intent(getContext(), AverageValuesActivity.class);
        startActivity(intent);
    }

    public void openMusterActivity(){
        Intent intent = new Intent(getContext(), MusterActivity.class);
        startActivity(intent);
    }


    public void openBestDayActivity(){
        Intent intent = new Intent(getContext(), BestDayActivity.class);
        startActivity(intent);
    }

}
