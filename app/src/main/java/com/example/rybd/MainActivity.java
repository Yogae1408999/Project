package com.example.rybd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
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

import com.example.rybd.databinding.ActivityMainBinding;
import com.example.rybd.ui.catatan.CatatanFragment;
import com.example.rybd.ui.gallery.GalleryFragment;
import com.example.rybd.ui.home.HomeFragment;
import com.example.rybd.ui.login.LoginFragment;
import com.example.rybd.ui.register.RegisterFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
//    public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActionBarDrawerToggle toogleMenu;
    DrawerLayout drawerLayout;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private RecyclerView recyclerView;
    private AdapterCatatan adapter;
    public ArrayList<CatatanHelper> catatanArrayList;
    ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.googleg_standard_color_18);
        actionbar.setTitle("Home");

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navView);
        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        String mEmail = sharedPreferences.getString("username", "");
        String mEmaill = sharedPreferences.getString("email", "");
        View headerView = navigationView.getHeaderView(0);
        toolbar.setNavigationOnClickListener(view -> {
            drawer.open();
        });
        if (mEmail == "") {
            navigationView.inflateMenu(R.menu.nav_header_menu);
        } else {
            TextView header_text = headerView.findViewById(R.id.header_title);
            TextView header_text_email = headerView.findViewById(R.id.header_title_email);
            header_text.setText(mEmail);
            header_text_email.setText(mEmaill);
            navigationView.inflateMenu(R.menu.nav_header_menu_2);
        }
        navigationView.setNavigationItemSelectedListener(this);
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.menu_satu, R.id.menu_dua, R.id.menu_tiga, R.id.menu_logout, R.id.menu_catatan)
//                .setOpenableLayout(drawer)
//                .build();
        FragmentManager fmx = getSupportFragmentManager();
        FragmentTransaction ftx = fmx.beginTransaction();
        ftx.replace(R.id.nav_host_fragment_content_main,new HomeFragment());
        ftx.commit();
//        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment_content_main);
        //        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        NavigationUI.setupActionBarWithNavController(this, navController,mAppBarConfiguration);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toogleMenu = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);

//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        setSupportActionBar(binding.appBarMain.toolbar);
//
//        DrawerLayout drawer = binding.drawerLayout;
//        NavigationView navigationView = binding.navView;
//        SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
//        String mEmail = sharedPreferences.getString("username","");
//        if(mEmail == ""){
//            navigationView.inflateMenu(R.menu.nav_header_menu);
//        }else{
//            navigationView.inflateMenu(R.menu.nav_header_menu_2);
//        }
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.menu_satu,R.id.menu_dua, R.id.menu_tiga,R.id.menu_logout)
//                .setOpenableLayout(drawer)
//                .build();
//
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
//        toogleMenu = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

//        TextView textEmail = findViewById(R.id.email);
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser mUser = mAuth.getCurrentUser();
//        String mUser = sharedPreferences.getString("username","");
//        String mEmail = sharedPreferences.getString("email","");
//        catatanArrayList = new ArrayList<>();

//        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
//        toolbar.setOnMenuItemClickListener(menu);

//        if(mEmail == ""){
//            textEmail.setText("Belum Login");
//        }else{
//            textEmail.setText(mEmail);
//        }
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myref = database.getReference("catatan");
//        myref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                catatanArrayList.clear();
//                if (mUser.equals(snapshot.child(mUser).getKey())){
//                    for (DataSnapshot data : snapshot.child(mUser).getChildren()){
//                        catatanArrayList.add(new CatatanHelper(data.child("id").getValue(Integer.class),data.child("judul").getValue(String.class), data.child("isian").getValue(String.class),data.child("tanggal").getValue(String.class),data.child("jam").getValue(String.class),data.child("remainder").getValue(String.class)));
//                    }
//                }
//                Collections.reverse(catatanArrayList);
//
//                adapter = new AdapterCatatan(catatanArrayList,MainActivity.this);
//                recyclerView = (RecyclerView) findViewById(R.id.listCatatan);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
//                recyclerView.setLayoutManager(layoutManager);
//                recyclerView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//
//            }
//        });
        FloatingActionButton ftb = findViewById(R.id.fab);
        ftb.setOnClickListener(view -> {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.nav_host_fragment_content_main,new CatatanFragment());
            ft.commit();
//            Intent intent2 = new Intent(MainActivity.this, AddcatatanActivity.class);
//            startActivity(intent2);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController,drawerLayout) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.menu_satu:
                fragment = new HomeFragment();
                break;
            case R.id.menu_dua:
                fragment = new LoginFragment();
                break;
            case R.id.menu_tiga:
                fragment = new RegisterFragment();
                break;
            case R.id.menu_logout:
                fragment = new LogoutFragment();
                break;
        }
        drawerLayout.close();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, fragment).commit();
        return true;
    }
}