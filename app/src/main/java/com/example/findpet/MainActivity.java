package com.example.findpet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ModelClass> petList;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initRecyclerView();
        

    }

    private void initRecyclerView() {

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(petList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void initData() {

        petList = new ArrayList<>();
        petList.add(new ModelClass("ABC","1 month"));
        petList.add(new ModelClass("DEF","2 months"));
        petList.add(new ModelClass("GHI","3 months"));
        petList.add(new ModelClass("JKL","1 month"));
        petList.add(new ModelClass("MNO","5 months"));
        petList.add(new ModelClass("ABC","1 month"));
        petList.add(new ModelClass("DEF","2 months"));
        petList.add(new ModelClass("GHI","3 months"));
        petList.add(new ModelClass("JKL","1 month"));
        petList.add(new ModelClass("MNO","5 months"));

    }
}