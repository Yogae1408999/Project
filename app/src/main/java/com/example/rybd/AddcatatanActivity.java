package com.example.rybd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddcatatanActivity extends AppCompatActivity {
    private static final String TAG = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcatatan);
        EditText isianJudul = findViewById(R.id.judul);
        EditText isianIsi = findViewById(R.id.isi);
        EditText isitanggal = findViewById(R.id.tanggal);
        EditText isijam = findViewById(R.id.jam);
        EditText isiremainder = findViewById(R.id.remainder);
        Button btnSimpan = findViewById(R.id.simpan);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("catatan");
        DatabaseReference myrefuser = database.getReference("user");
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                String format = "yyyy/MM/dd";
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                isitanggal.setText(sdf.format(calendar.getTime()));
            }
        };

        isitanggal.setOnClickListener(view -> {
            new DatePickerDialog(AddcatatanActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        isijam.setOnClickListener(view -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(AddcatatanActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    calendar.set(Calendar.HOUR_OF_DAY,selectedHour);
                    calendar.set(Calendar.MINUTE,selectedMinute);
                    String format = "HH:mm";
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    isijam.setText(sdf.format(calendar.getTime()));
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });
        isiremainder.setOnClickListener(view -> {
            String[] listitem = new String[]{"Tidak ada","5 Menit","10 Menit","30 Menit","1 jam","2 jam","3 jam"};
            AlertDialog.Builder mBuil = new AlertDialog.Builder(AddcatatanActivity.this);
            mBuil.setTitle("Silahkan pilih remainder");
            mBuil.setSingleChoiceItems(listitem, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    isiremainder.setText(listitem[i]);
                    dialogInterface.dismiss();
                }
            });
            mBuil.setNeutralButton("cencel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog mDialog = mBuil.create();
            mDialog.show();
        });
        btnSimpan.setOnClickListener(view -> {

            String judul = isianJudul.getText().toString();
            String isi = isianIsi.getText().toString();
            String tanggal = isitanggal.getText().toString();
            String jam = isijam.getText().toString();
            String remainder = isiremainder.getText().toString();
//            FirebaseAuth mAuth = FirebaseAuth.getInstance();
//            FirebaseUser mUser = mAuth.getCurrentUser();
            SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
            String mEmail = sharedPreferences.getString("username","");
            String nama = mEmail;

            if(mEmail == ""){
                Toast.makeText(this, "Silahkan Login", Toast.LENGTH_LONG).show();
                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }else{
                myref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (judul.length() == 0) {
                            isianJudul.setError("Tidak boleh kosong");
                        }else if(isi.length() == 0){
                            isianIsi.setError("Tidak boleh kosong");
                        }else if(tanggal.length() == 0){
                            isitanggal.setError("Tidak boleh kosong");
                        }else if(jam.length() == 0){
                            isijam.setError("Tidak boleh kosong");
                        }else if(remainder.length() == 0){
                            isiremainder.setError("Tidak boleh kosong");
                        }else if (mEmail.equals(snapshot.child(mEmail).getKey())){
                            Integer Jumlah = 0;
                            for (DataSnapshot data : snapshot.child(mEmail).getChildren()){
                                Jumlah = data.child("id").getValue(Integer.class)+1;
                            }
                            CatatanHelper catatanHelper = new CatatanHelper(Jumlah,judul,isi,tanggal,jam,remainder);
                            myref.child(nama).child(Jumlah.toString()).setValue(catatanHelper);
                            Toast.makeText(AddcatatanActivity.this,"berhasil menmbahkan",Toast.LENGTH_SHORT).show();
                            Calendar date = Calendar.getInstance();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                            try {
                                date.setTime(format.parse(tanggal+jam));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
//                            Calendar cal = new GregorianCalendar();
//                            cal.add(Calendar.DAY_OF_YEAR, date.YEAR);
//                            cal.set(Calendar.HOUR_OF_DAY, date.HOUR_OF_DAY);
//                            cal.set(Calendar.MINUTE, date.MINUTE);
//                            cal.set(Calendar.SECOND, date.SECOND);
//                            cal.set(Calendar.MILLISECOND, date.MILLISECOND);
//                            cal.set(Calendar.DATE, date.DATE);
//                            cal.set(Calendar.MONTH, date.MONTH);
                            finish();
                            Intent intent = new Intent(AddcatatanActivity.this, MainActivity.class);
                            startActivity(intent);
//
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                CharSequence name = "remander";
                                String desc = "Chanelku";
                                Integer importx = NotificationManager.IMPORTANCE_HIGH;
                                NotificationChannel channel = new NotificationChannel("alarm",name,importx);
                                channel.setDescription(desc);

                                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                notificationManager.createNotificationChannel(channel);
                            }
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            Intent intent1 = new Intent(AddcatatanActivity.this,MyBroadcastReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(AddcatatanActivity.this,0,intent1,0);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (5 * 1000), pendingIntent);
                        }
                    }

                    @Override
                    public void onCancelled( DatabaseError error) {

                    }
                });


            }
        });
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
//        String mEmail = sharedPreferences.getString("email","");
//
//        if(mEmail == ""){
//            inflater.inflate(R.menu.optionmenu,menu);
//        }else{
//            inflater.inflate(R.menu.optionmenu2,menu);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.home:
//                finish();
//                Intent intentHome = new Intent(this, MainActivity.class);
//                startActivity(intentHome);
//                return true;
//            case R.id.login:
//                finish();
//                Intent intent = new Intent(this, LoginActivity.class);
//                startActivity(intent);
//                return true;
//            case R.id.register:
//                finish();
//                Intent intent2 = new Intent(this, RegisterActivity.class);
//                startActivity(intent2);
//                return true;
//            case R.id.logout:
//                SharedPreferences preferences = getSharedPreferences("Login",AddcatatanActivity.this.MODE_PRIVATE);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.clear();
//                editor.apply();
//                finish();
//                Intent intent3 = new Intent(this,LoginActivity.class);
//                startActivity(intent3);
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}