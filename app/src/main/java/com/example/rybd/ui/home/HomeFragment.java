package com.example.rybd.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rybd.AdapterCatatan;
import com.example.rybd.CatatanHelper;
import com.example.rybd.CatatanViewClick;
import com.example.rybd.R;
import com.example.rybd.databinding.FragmentHomeBinding;
import com.example.rybd.ui.catatan.EditCatatanFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment implements CatatanViewClick {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private AdapterCatatan adapter;
    public ArrayList<CatatanHelper> catatanArrayList;
    Integer status;
    ArrayList<String> data;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("Home");

        TabLayout tab = getActivity().findViewById(R.id.tablayout);
        tab.setVisibility(View.VISIBLE);
        ExtendedFloatingActionButton ftb = getActivity().findViewById(R.id.fab);
        ftb.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        String mUser = sharedPreferences.getString("username","");
        catatanArrayList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("catatan");
        Bundle bdl = this.getArguments();

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (bdl != null) {
                    status = bdl.getInt("status");
                }
                catatanArrayList.clear();
                if (mUser.equals(snapshot.child(mUser).getKey())){
                    for (DataSnapshot data : snapshot.child(mUser).getChildren()){
                        if (data.child("aktif").getValue(Integer.class) == status){
                            catatanArrayList.add(new CatatanHelper(data.child("id").getValue(Integer.class),data.child("judul").getValue(String.class), data.child("isian").getValue(String.class),data.child("tanggal").getValue(String.class),data.child("jam").getValue(String.class),data.child("remainder").getValue(String.class),data.child("aktif").getValue(Integer.class)));
                        }
                    }
                }
                Collections.reverse(catatanArrayList);

                adapter = new AdapterCatatan(catatanArrayList, HomeFragment.this);
                recyclerView = (RecyclerView) root.findViewById(R.id.listCatatan);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext() );
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle bundle = new Bundle();
        Fragment frag = new EditCatatanFragment();
        frag.setArguments(bundle);
        bundle.putInt("data", Integer.parseInt(catatanArrayList.get(position).getId().toString()));
        ft.replace(R.id.nav_host_fragment_content_main,frag);
        ft.commit();
//        Intent intentnota = new Intent(getActivity(),EditCatatan.class);
//        intentnota.putExtra("data",catatanArrayList.get(position).getId().toString());
//        startActivity(intentnota);
    }

    @Override
    public void onLongItemClick(int position) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("catatan");
        SharedPreferences sharedPreferences = this.getContext().getSharedPreferences("Login",getContext().MODE_PRIVATE);
        String mUser = sharedPreferences.getString("username","");

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                snapshot.child(mUser).child(catatanArrayList.get(position).getId().toString()).getRef().removeValue();
                Toast.makeText(getContext(), "Berhasil Hapus : "+catatanArrayList.get(position).getJudul(),Toast.LENGTH_SHORT).show();
                catatanArrayList.remove(position);
                adapter.notifyItemRemoved(position);
                adapter = new AdapterCatatan(catatanArrayList,HomeFragment.this);
                recyclerView = (RecyclerView) binding.getRoot().findViewById(R.id.listCatatan);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });
    }

}