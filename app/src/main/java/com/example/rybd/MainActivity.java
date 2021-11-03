package com.example.rybd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterCatatan adapter;
    private ArrayList<Catatan> catatanArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addData();

        recyclerView = (RecyclerView) findViewById(R.id.listCatatan);

        adapter = new AdapterCatatan(catatanArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

    }

    private void addData() {
        catatanArrayList = new ArrayList<>();
        catatanArrayList.add(new Catatan("Dimas Maulana", "1414370309", "123456789"));
        catatanArrayList.add(new Catatan("Fadly Yonk", "1214234560", "987654321"));
        catatanArrayList.add(new Catatan("Ariyandi Nugraha", "1214230345", "987648765"));
        catatanArrayList.add(new Catatan("Aham Siswana", "1214378098", "098758124"));
    }
}