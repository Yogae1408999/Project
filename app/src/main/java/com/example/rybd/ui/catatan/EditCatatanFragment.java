package com.example.rybd.ui.catatan;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rybd.CatatanHelper;
import com.example.rybd.MyBroadcastReceiver;
import com.example.rybd.MyBroadcastRemainderReceiver;
import com.example.rybd.R;
import com.example.rybd.ui.home.HomeFragment;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditCatatanFragment extends Fragment {
    long setremainder = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View binding = inflater.inflate(R.layout.activity_addcatatan, container, false);
        Bundle bundle = this.getArguments();
        String data = String.valueOf(bundle.getInt("data"));
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("Edit Catatan");
        TabLayout tab = getActivity().findViewById(R.id.tablayout);
        tab.setVisibility(View.GONE);
        ExtendedFloatingActionButton ftb = getActivity().findViewById(R.id.fab);
        ftb.setVisibility(View.GONE);
        TextInputLayout vJudul = binding.findViewById(R.id.input_judul);
        TextInputLayout vIsi = binding.findViewById(R.id.input_desc);
        TextInputLayout vtanggal = binding.findViewById(R.id.input_tanggal);
        TextInputLayout vjam = binding.findViewById(R.id.input_time);
        TextInputLayout vremainder = binding.findViewById(R.id.input_remainder);

        TextInputEditText isianJudul = binding.findViewById(R.id.edit_judul);
        TextInputEditText isianIsi = binding.findViewById(R.id.edit_desc);
        TextInputEditText isitanggal = binding.findViewById(R.id.edit_tanggal);
        TextInputEditText isijam = binding.findViewById(R.id.edit_time);
        TextInputEditText isiremainder = binding.findViewById(R.id.edit_remainder);
        Button btnSimpan = binding.findViewById(R.id.simpan);
        btnSimpan.setText("Simpan");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("catatan");
        DatabaseReference myrefuser = database.getReference("user");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login",getActivity().MODE_PRIVATE);
        String mUser = sharedPreferences.getString("username","");
        String mEmail = sharedPreferences.getString("email","");
        Calendar calendar = Calendar.getInstance();

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (mUser.equals(snapshot.child(mUser).getKey())){
                    isianJudul.setText(snapshot.child(mUser).child(data).child("judul").getValue(String.class));
                    isianIsi.setText(snapshot.child(mUser).child(data).child("isian").getValue(String.class));
                    isitanggal.setText(snapshot.child(mUser).child(data).child("tanggal").getValue(String.class));
                    isijam.setText(snapshot.child(mUser).child(data).child("jam").getValue(String.class));
                    isiremainder.setText(snapshot.child(mUser).child(data).child("remainder").getValue(String.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
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
            new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        isijam.setOnClickListener(view -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    calendar.set(Calendar.HOUR_OF_DAY,selectedHour);
                    calendar.set(Calendar.MINUTE,selectedMinute);
                    calendar.set(Calendar.MILLISECOND,0);
                    calendar.set(Calendar.SECOND,0);
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
            AlertDialog.Builder mBuil = new AlertDialog.Builder(getContext());
            mBuil.setTitle("Silahkan pilih remainder");
            mBuil.setSingleChoiceItems(listitem, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0){
                        setremainder = 0;
                    }else if(i == 1){
                        setremainder = 300000;
                    }else if(i == 2){
                        setremainder = 600000;
                    }else if(i == 3){
                        setremainder = 1800000;
                    }else if(i == 4){
                        setremainder = 3600000;
                    }else if(i == 5){
                        setremainder = 7200000;
                    }else if(i == 6){
                        setremainder = 10800000;
                    }
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
            Integer aktif = 1;

//            FirebaseAuth mAuth = FirebaseAuth.getInstance();
//            FirebaseUser mUser = mAuth.getCurrentUser();
            String nama = mUser;

            if(mEmail == ""){
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment frag = new LoginFragment();
                ft.replace(R.id.nav_host_fragment_content_main,frag);
                ft.commit();
            }else{
                myref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (judul.length() == 0) {
                            vJudul.setError("Tidak boleh kosong");
                        }else if(isi.length() == 0){
                            vIsi.setError("Tidak boleh kosong");
                        }else if(tanggal.length() == 0){
                            vtanggal.setError("Tidak boleh kosong");
                        }else if(jam.length() == 0){
                            vjam.setError("Tidak boleh kosong");
                        }else if(remainder.length() == 0) {
                            vremainder.setError("Tidak boleh kosong");
                        }else if (mUser.equals(snapshot.child(mUser).getKey())){
                            Integer Jumlah = Integer.parseInt(data);
                            CatatanHelper catatanHelper = new CatatanHelper(Jumlah,judul,isi,tanggal,jam,remainder,aktif);
                            myref.child(nama).child(Jumlah.toString()).setValue(catatanHelper);
                            Toast.makeText(getContext(),"berhasil Merubah",Toast.LENGTH_SHORT).show();
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            Bundle bundle = new Bundle();
                            Fragment frag = new HomeFragment();
                            bundle.putInt("status",1);
                            frag.setArguments(bundle);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.nav_host_fragment_content_main, frag);
                            ft.replace(R.id.nav_host_fragment_content_main,frag);
                            ft.commit();
                            TabLayout tab = getActivity().findViewById(R.id.tablayout);
                            tab.selectTab(tab.getTabAt(0));
                            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
                            Intent intent1 = new Intent(getContext(), MyBroadcastReceiver.class);
                            if(setremainder>0){
                                Intent setIntenttRemainder = new Intent(getContext(), MyBroadcastRemainderReceiver.class);
                                setIntenttRemainder.putExtra("judul",judul);
                                setIntenttRemainder.putExtra("jumlah",Jumlah);
                                setIntenttRemainder.putExtra("waktu",remainder);
                                PendingIntent pendingIntenttRemainder = PendingIntent.getBroadcast(getContext(),Jumlah,setIntenttRemainder,0);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()-setremainder, pendingIntenttRemainder);
                                }
                            }
                            intent1.putExtra("judul",judul);
                            intent1.putExtra("jumlah",Jumlah);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),Jumlah,intent1,0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            }
                            Log.i("sekarang", String.valueOf(System.currentTimeMillis()));
                            Log.i("custom", String.valueOf(calendar.getTimeInMillis()));
                        }
                    }
                    @Override
                    public void onCancelled( DatabaseError error) {

                    }
                });


            }
        });

        return binding;
    }
}
