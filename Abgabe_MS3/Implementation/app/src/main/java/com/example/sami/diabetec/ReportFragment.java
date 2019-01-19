package com.example.sami.diabetec;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ReportFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);


        ArrayList<ExampleItem> exampleList = new ArrayList<>();
        exampleList.add(new ExampleItem("Muster", "Schau Dir an, an wann Deine Blutzuckerwerte ein Muster aufweisen."));
        exampleList.add(new ExampleItem("Bester Tag", "Schau Dir Deinen besten Tag an."));
        exampleList.add(new ExampleItem("Durchschnittswerte", "Schau Dir Deine Durchschnittswerte an."));
        exampleList.add(new ExampleItem("Durchschnittstag", "Schau Dir Deinen durchschnittlichen Tag an."));
        exampleList.add(new ExampleItem("Überlagerung", "Schau Dir alle Graphen der letzten 7 Tage an."));
        exampleList.add(new ExampleItem("Muster", "Schau Dir an, an wann Deine Blutzuckerwerte ein Muster aufweisen."));
        exampleList.add(new ExampleItem("Bester Tag", "Schau Dir Deinen besten Tag an."));
        exampleList.add(new ExampleItem("Durchschnittswerte", "Schau Dir Deine Durchschnittswerte an."));
        exampleList.add(new ExampleItem("Durchschnittstag", "Schau Dir Deinen durchschnittlichen Tag an."));
        exampleList.add(new ExampleItem("Überlagerung", "Schau Dir alle Graphen der letzten 7 Tage an."));

        mRecyclerView = view.findViewById(R.id.recycler_view_reports);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ExampleAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}
