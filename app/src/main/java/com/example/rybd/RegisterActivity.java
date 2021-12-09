package com.example.rybd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        EditText inputNama = findViewById(R.id.nama);
//        EditText inputEmail = findViewById(R.id.email);
//        EditText inputPassword = findViewById(R.id.password);
//        EditText inputConfrimpassword = findViewById(R.id.confirmpassword);
//        Button btnRegister = findViewById(R.id.btnregister);
//        Button btnLogin = findViewById(R.id.btnlogin);
//        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//        String namaPattern = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){1,18}[a-zA-Z0-9]$";
//        Pattern pattern = Pattern.compile(namaPattern);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myref = database.getReference("user");

//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser mUser = mAuth.getCurrentUser();
//        btnRegister.setOnClickListener(view -> {
//            String nama = inputNama.getText().toString();
//            String email = inputEmail.getText().toString();
//            String password = inputPassword.getText().toString();
//            String confrimpassword = inputConfrimpassword.getText().toString();
//
//            RegisterHelper registerHelper = new RegisterHelper(email, password, nama);
//            myref.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    for (DataSnapshot data: snapshot.getChildren()) {
//                        String getEmail = data.child("email").getValue(String.class);
//                        if (email.equals(getEmail)){
//                            inputEmail.setError("Sudah Digunakan");
//                        }else if (pattern.matcher(nama).matches() == false){
//                            inputNama.setError("username tidak boleh spasi");
//                        }else if(snapshot.hasChild(nama)){
//                            inputNama.setError("username sudah ada");
//                        }else if(!email.matches(emailPattern)){
//                            inputEmail.setError("Gunakan Email Asli");
//                        }else if(password.isEmpty() || password.length() < 6){
//                            inputPassword.setError("password kurang dari 6");
//                        }else if(!password.equals(confrimpassword)){
//                            inputConfrimpassword.setError("password tidak cocok");
//                        }else if(!email.equals(getEmail) && pattern.matcher(nama).matches() && !snapshot.hasChild(nama) && email.matches(emailPattern) && !(password.isEmpty() || password.length() < 6) && password.equals(confrimpassword)){
//                            finish();
//                            Intent intent2 = new Intent(RegisterActivity.this, MainActivity.class);
//                            startActivity(intent2);
//                            Toast.makeText(RegisterActivity.this, "Berhasil Mendaftar",Toast.LENGTH_SHORT).show();
//                            myref.child(nama).setValue(registerHelper);
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//
//                }
//            });
//            if(nama.isEmpty()){
//                inputNama.setError("Nama Kosong");
//            }else if(!email.matches(emailPattern)){
//                inputEmail.setError("Gunakan Email Asli");
//            }else if(password.isEmpty() || password.length() < 6){
//                inputPassword.setError("password kurang dari 6");
//            }else if(!password.equals(confrimpassword)){
//                inputConfrimpassword.setError("password tidak cocok");
//            }else{
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("HHmmssZddMMMyyyy", Locale.getDefault());
//                String formattedDate = df.format(c);
//                myref.child(formattedDate).setValue(registerHelper);
//                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            Toast toast2 =  Toast.makeText(RegisterActivity.this, "Sukses Registrasi",Toast.LENGTH_SHORT);
//                            toast2.show();
//                        }else{
//                            Toast toast2 =  Toast.makeText(RegisterActivity.this, ""+task.getException(),Toast.LENGTH_SHORT);
//                            toast2.show();
//                        }
//                    }
//                });
//            }

//        });
//        btnLogin.setOnClickListener(v -> {
//            finish();
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//        });

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
            case R.id.login:
                finish();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.home:
                finish();
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}