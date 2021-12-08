package com.example.rybd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
        EditText inputEmail = findViewById(R.id.email);
        EditText inputPassword = findViewById(R.id.password);
        Button btnLogin = findViewById(R.id.btnlogin);
        Button btnRegister = findViewById(R.id.btnregister);
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser mUser = mAuth.getCurrentUser();
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
                              finish();
                              Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                              startActivity(intent2);
                              Toast.makeText(LoginActivity.this, "Berhasil Login", Toast.LENGTH_LONG).show();
                          } else {
                              Toast.makeText(LoginActivity.this, "Password salah", Toast.LENGTH_LONG).show();
                          }
                      }
                  }else{
                      Toast.makeText(LoginActivity.this, "Username tidak ditemukan", Toast.LENGTH_LONG).show();
                  }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
//            if(!email.matches(emailPattern)){
//                inputEmail.setError("Gunakan Email Asli");
//            }else if(password.isEmpty() || password.length() < 6){
//                inputPassword.setError("password kurang dari 6");
//            }else{
//                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            Toast toast2 =  Toast.makeText(LoginActivity.this, "Sukses Login",Toast.LENGTH_SHORT);
//                            toast2.show();
//                        }else{
//                            Toast toast2 =  Toast.makeText(LoginActivity.this, ""+task.getException(),Toast.LENGTH_SHORT);
//                            toast2.show();
//                        }
//                    }
//                });
//            }
        });

        btnRegister.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if(mUser == null){
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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.register:
                finish();
                Intent intent2 = new Intent(this, RegisterActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}