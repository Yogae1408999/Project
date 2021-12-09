package com.example.rybd.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rybd.AdapterCatatan;
import com.example.rybd.CatatanHelper;
import com.example.rybd.CatatanViewClick;
import com.example.rybd.EditCatatan;
import com.example.rybd.MainActivity;
import com.example.rybd.R;
import com.example.rybd.databinding.FragmentHomeBinding;
import com.example.rybd.ui.catatan.CatatanFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment implements CatatanViewClick, View.OnClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private AdapterCatatan adapter;
    public ArrayList<CatatanHelper> catatanArrayList;
    ArrayList<String> data;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        TextView textEmail = this.getActivity().findViewById(R.id.email);
        String mUser = sharedPreferences.getString("username","");
        String mEmail = sharedPreferences.getString("email","");
        catatanArrayList = new ArrayList<>();
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
        FloatingActionButton ftb = getActivity().findViewById(R.id.fab);
//        ftb.setOnClickListener(view -> {
//            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.nav_host_fragment_content_main, new CatatanFragment());
//            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            ft.addToBackStack(null);
//            ft.commit();
//        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position) {
        Intent intentnota = new Intent(getActivity(),EditCatatan.class);
        intentnota.putExtra("data",catatanArrayList.get(position).getId().toString());
        startActivity(intentnota);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                Toast.makeText(getContext(),"asdasd",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}