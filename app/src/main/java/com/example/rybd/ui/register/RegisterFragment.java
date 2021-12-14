package com.example.rybd.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rybd.R;
import com.example.rybd.RegisterHelper;
import com.example.rybd.ui.login.LoginFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("Register");
        TabLayout tab = getActivity().findViewById(R.id.tablayout);
        tab.setVisibility(View.GONE);
        ExtendedFloatingActionButton ftb = getActivity().findViewById(R.id.fab);
        ftb.setVisibility(View.GONE);
        TextInputLayout vNama = binding.findViewById(R.id.input_username);
        TextInputLayout vEmail = binding.findViewById(R.id.input_email);
        TextInputLayout vPassword = binding.findViewById(R.id.input_password);
        TextInputLayout vConfrimpassword = binding.findViewById(R.id.input_conf_password);

        TextInputEditText inputNama = binding.findViewById(R.id.edit_user);
        TextInputEditText inputEmail = binding.findViewById(R.id.edit_email);
        TextInputEditText inputPassword = binding.findViewById(R.id.edit_password);
        TextInputEditText inputConfrimpassword = binding.findViewById(R.id.edit_conf_password);
        Button btnRegister = binding.findViewById(R.id.btnregister);
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
                            vEmail.setError("Sudah Digunakan");
                        }else if (pattern.matcher(nama).matches() == false){
                            vNama.setError("username tidak boleh spasi");
                        }else if(snapshot.hasChild(nama)){
                            vNama.setError("username sudah ada");
                        }else if(!email.matches(emailPattern)){
                            vEmail.setError("Gunakan Email Asli");
                        }else if(password.isEmpty() || password.length() < 6){
                            vPassword.setError("password kurang dari 6");
                        }else if(!password.equals(confrimpassword)){
                            vConfrimpassword.setError("password tidak cocok");
                        }else if(!email.equals(getEmail) && pattern.matcher(nama).matches() && !snapshot.hasChild(nama) && email.matches(emailPattern) && !(password.isEmpty() || password.length() < 6) && password.equals(confrimpassword)){
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.nav_host_fragment_content_main, new LoginFragment());
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
