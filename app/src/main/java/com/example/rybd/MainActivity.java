package com.example.rybd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements CatatanViewClick{
    private RecyclerView recyclerView;
    private AdapterCatatan adapter;
    public ArrayList<CatatanHelper> catatanArrayList;
    ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton addCatatans = findViewById(R.id.floatingActionButton);
        SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);

        TextView textEmail = findViewById(R.id.email);
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser mUser = mAuth.getCurrentUser();
        String mUser = sharedPreferences.getString("username","");
        String mEmail = sharedPreferences.getString("email","");
        catatanArrayList = new ArrayList<>();

        if(mEmail == ""){
            textEmail.setText("Belum Login");
        }else{
            textEmail.setText(mEmail);
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("catatan");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                catatanArrayList.clear();
                if (mUser.equals(snapshot.child(mUser).getKey())){
                    for (DataSnapshot data : snapshot.child(mUser).getChildren()){
                        catatanArrayList.add(new CatatanHelper(data.child("id").getValue(Integer.class),data.child("judul").getValue(String.class), data.child("isian").getValue(String.class),data.child("tanggal").getValue(String.class),data.child("jam").getValue(String.class),data.child("remainder").getValue(String.class)));
                    }
                }
                Collections.reverse(catatanArrayList);

                adapter = new AdapterCatatan(catatanArrayList,MainActivity.this);
                recyclerView = (RecyclerView) findViewById(R.id.listCatatan);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        addCatatans.setOnClickListener(view -> {
            finish();
            Intent intent = new Intent(this, AddcatatanActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser mUser = mAuth.getCurrentUser();
        SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
        String mEmail = sharedPreferences.getString("email","");

        if(mEmail == ""){
            inflater.inflate(R.menu.optionmenu,menu);
        }else{
            inflater.inflate(R.menu.optionmenu2,menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                finish();
                Intent intentHome = new Intent(this, MainActivity.class);
                startActivity(intentHome);
                return true;
            case R.id.login:
                finish();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.register:
                finish();
                Intent intent2 = new Intent(this, RegisterActivity.class);
                startActivity(intent2);
                return true;
            case R.id.logout:
                SharedPreferences preferences = getSharedPreferences("Login",MainActivity.this.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                finish();
                Intent intent3 = new Intent(this,LoginActivity.class);
                startActivity(intent3);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intentnota = new Intent(this,EditCatatan.class);
//        data = new ArrayList<>();
//        data.add(catatanArrayList.get(position).getId().toString());
        intentnota.putExtra("data",catatanArrayList.get(position).getId().toString());
        startActivity(intentnota);
    }

    @Override
    public void onLongItemClick(int position) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("catatan");
        SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
        String mUser = sharedPreferences.getString("username","");

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                snapshot.child(mUser).child(catatanArrayList.get(position).getId().toString()).getRef().removeValue();
                Toast.makeText(MainActivity.this, "Berhasil Hapus : "+catatanArrayList.get(position).getJudul(),Toast.LENGTH_SHORT).show();
                catatanArrayList.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });

    }
}