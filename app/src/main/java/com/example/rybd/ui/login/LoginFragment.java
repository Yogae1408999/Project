package com.example.rybd.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rybd.LoginActivity;
import com.example.rybd.MainActivity;
import com.example.rybd.R;
import com.example.rybd.RegisterActivity;
import com.example.rybd.databinding.FragmentGalleryBinding;
import com.example.rybd.ui.home.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {
    private View binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = inflater.inflate(R.layout.activity_login, container, false);
        SharedPreferences sharedPreferences = binding.getContext().getSharedPreferences("Login",getContext().MODE_PRIVATE);
        EditText inputEmail = binding.findViewById(R.id.email);
        EditText inputPassword = binding.findViewById(R.id.password);
        Button btnLogin = binding.findViewById(R.id.btnlogin);
        Button btnRegister = binding.findViewById(R.id.btnregister);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("user");
        btnLogin.setOnClickListener(v -> {
            String username = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            Query query = myref.orderByChild("nama").equalTo(username);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot user : snapshot.getChildren()) {
                            String getPassword = user.child("password").getValue(String.class);
                            String getEmail = user.child("email").getValue(String.class);

                            if (password.equals(getPassword)) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username",username);
                                editor.putString("email",getEmail);
                                editor.apply();
                                Intent intent2 = new Intent(getContext(), MainActivity.class);
                                startActivity(intent2);
                                getActivity().finish();
                                Toast.makeText(getContext(), "Berhasil Login", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "Password salah", Toast.LENGTH_LONG).show();
                            }
                        }
                    }else{
                        Toast.makeText(getContext(), "Username tidak ditemukan", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        });


        return binding;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}