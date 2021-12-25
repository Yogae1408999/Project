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
import com.example.rybd.ui.about.AboutFragment;
import com.example.rybd.ui.catatan.CatatanFragment;
import com.example.rybd.ui.home.HomeFragment;
import com.example.rybd.ui.login.LoginFragment;
import com.example.rybd.ui.register.RegisterFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
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

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.outline_menu_24);
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

        Bundle bundle = new Bundle();
        Fragment frag5 = new HomeFragment();
        bundle.putInt("status",1);
        frag5.setArguments(bundle);
        FragmentManager fmx = getSupportFragmentManager();
        FragmentTransaction ftx = fmx.beginTransaction();
        ftx.replace(R.id.nav_host_fragment_content_main,frag5);
        ftx.commit();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toogleMenu = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        ExtendedFloatingActionButton ftb = findViewById(R.id.fab);
        ftb.setOnClickListener(view -> {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.nav_host_fragment_content_main,new CatatanFragment());
            ft.commit();

        });
        TabLayout tab = findViewById(R.id.tablayout);
        tab.selectTab(tab.getTabAt(0));

        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        Fragment frag = new HomeFragment();
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        bundle.putInt("status",1);
                        ft.replace(R.id.nav_host_fragment_content_main,frag);
                        frag.setArguments(bundle);
                        ft.commit();
                        return;
                    case 1:
                        Fragment frag1 = new HomeFragment();
                        FragmentManager fm1 = getSupportFragmentManager();
                        FragmentTransaction ft1 = fm1.beginTransaction();
                        bundle.putInt("status",0);
                        ft1.replace(R.id.nav_host_fragment_content_main,frag1);
                        frag1.setArguments(bundle);
                        ft1.commit();
                        return;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController,drawerLayout) || super.onSupportNavigateUp();
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.menu_satu:
                Bundle bundle = new Bundle();
                fragment = new HomeFragment();
                bundle.putInt("status",1);
                fragment.setArguments(bundle);
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
            case R.id.menu_about:
                fragment = new AboutFragment();
                break;
        }
        if (item.isCheckable()){
            item.setCheckable(false);
        }
        item.setCheckable(true);
        TabLayout tab = findViewById(R.id.tablayout);
        tab.selectTab(tab.getTabAt(0));
        drawerLayout.close();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, fragment).commit();
        return true;
    }
}