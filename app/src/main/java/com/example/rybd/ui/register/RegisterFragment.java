package com.example.rybd.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rybd.MainActivity;
import com.example.rybd.R;
import com.example.rybd.RegisterActivity;
import com.example.rybd.RegisterHelper;
import com.example.rybd.ui.gallery.GalleryFragment;
import com.example.rybd.ui.home.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View binding = inflater.inflate(R.layout.activity_register, container, false);

        EditText inputNama = binding.findViewById(R.id.nama);
        EditText inputEmail = binding.findViewById(R.id.email);
        EditText inputPassword = binding.findViewById(R.id.password);
        EditText inputConfrimpassword = binding.findViewById(R.id.confirmpassword);
        Button btnRegister = binding.findViewById(R.id.btnregister);
        Button btnLogin = binding.findViewById(R.id.btnlogin);
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String namaPattern = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){1,18}[a-zA-Z0-9]$";
        Pattern pattern = Pattern.compile(namaPattern);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("user");
        btnRegister.setOnClickListener(view -> {
            String nama = inputNama.getText().toString();
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            String confrimpassword = inputConfrimpassword.getText().toString();

            RegisterHelper registerHelper = new RegisterHelper(email, password, nama);
            myref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot data: snapshot.getChildren()) {
                        String getEmail = data.child("email").getValue(String.class);
                        if (email.equals(getEmail)){
                            inputEmail.setError("Sudah Digunakan");
                        }else if (pattern.matcher(nama).matches() == false){
                            inputNama.setError("username tidak boleh spasi");
                        }else if(snapshot.hasChild(nama)){
                            inputNama.setError("username sudah ada");
                        }else if(!email.matches(emailPattern)){
                            inputEmail.setError("Gunakan Email Asli");
                        }else if(password.isEmpty() || password.length() < 6){
                            inputPassword.setError("password kurang dari 6");
                        }else if(!password.equals(confrimpassword)){
                            inputConfrimpassword.setError("password tidak cocok");
                        }else if(!email.equals(getEmail) && pattern.matcher(nama).matches() && !snapshot.hasChild(nama) && email.matches(emailPattern) && !(password.isEmpty() || password.length() < 6) && password.equals(confrimpassword)){
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.nav_host_fragment_content_main, new GalleryFragment());
                            transaction.addToBackStack(null);
                            transaction.commit();
                            Toast.makeText(getContext(), "Berhasil Mendaftar",Toast.LENGTH_SHORT).show();
                            myref.child(nama).setValue(registerHelper);
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        });
        return binding;
    }
}
