package com.example.rybd.ui.catatan;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rybd.AddcatatanActivity;
import com.example.rybd.CatatanHelper;
import com.example.rybd.MainActivity;
import com.example.rybd.MyBroadcastReceiver;
import com.example.rybd.R;
import com.example.rybd.ui.home.HomeFragment;
import com.example.rybd.ui.login.LoginFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CatatanFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View binding = inflater.inflate(R.layout.activity_addcatatan, container, false);

        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("Tambah Catatan");

        EditText isianJudul = binding.findViewById(R.id.judul);
        EditText isianIsi = binding.findViewById(R.id.isi);
        EditText isitanggal = binding.findViewById(R.id.tanggal);
        EditText isijam = binding.findViewById(R.id.jam);
        EditText isiremainder = binding.findViewById(R.id.remainder);
        Button btnSimpan = binding.findViewById(R.id.simpan);
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
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login",getActivity().MODE_PRIVATE);
            String mEmail = sharedPreferences.getString("username","");
            String nama = mEmail;

            if(mEmail == ""){
                Toast.makeText(getContext(), "Silahkan Login", Toast.LENGTH_LONG).show();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_main, new LoginFragment());
                transaction.addToBackStack(null);
                transaction.commit();
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
                            Toast.makeText(getContext(),"berhasil menmbahkan",Toast.LENGTH_SHORT).show();
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
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.nav_host_fragment_content_main, new HomeFragment());
                            transaction.addToBackStack(null);
                            transaction.commit();
//
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                CharSequence name = "remander";
                                String desc = "Chanelku";
                                Integer importx = NotificationManager.IMPORTANCE_HIGH;
                                NotificationChannel channel = new NotificationChannel("alarm",name,importx);
                                channel.setDescription(desc);

                                NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
                                notificationManager.createNotificationChannel(channel);
                            }
                            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
                            Intent intent1 = new Intent(getContext(), MyBroadcastReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),0,intent1,0);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (20 * 1000), pendingIntent);
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
