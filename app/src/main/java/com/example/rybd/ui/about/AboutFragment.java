package com.example.rybd.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.rybd.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AboutFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View binding = inflater.inflate(R.layout.activity_about, container, false);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("Tentang Kami");
        TabLayout tab = getActivity().findViewById(R.id.tablayout);
        tab.setVisibility(View.GONE);
        ExtendedFloatingActionButton ftb = getActivity().findViewById(R.id.fab);
        ftb.setVisibility(View.GONE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("about");
        TextView versi = binding.findViewById(R.id.versi);
        TextView me1 = binding.findViewById(R.id.me1);
        TextView me2 = binding.findViewById(R.id.me2);
        TextView me3 = binding.findViewById(R.id.me3);
        TextView me4 = binding.findViewById(R.id.me4);
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                versi.setText(snapshot.child("versi").getValue(String.class));
                me1.setText(snapshot.child("me1").getValue(String.class));
                me2.setText(snapshot.child("me2").getValue(String.class));
                me3.setText(snapshot.child("me3").getValue(String.class));
                me4.setText(snapshot.child("me4").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding;
    }
}
