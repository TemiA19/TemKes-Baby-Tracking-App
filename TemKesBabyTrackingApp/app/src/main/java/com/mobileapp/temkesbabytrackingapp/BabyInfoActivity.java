package com.mobileapp.temkesbabytrackingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BabyInfoActivity<FirebaseFirestore> extends AppCompatActivity {
;
    final Calendar myCalendar = Calendar.getInstance();
    EditText editText;
    private TextView save_btn;
    CircleImageView add_baby;
    private EditText reg_babyname, baby_birthday, baby_weight;
    Spinner baby_gender;
    Uri imageUri;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    DatabaseReference reference;
    BabyUser babyUser;
    private Map newPost = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_info);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("babyUser");
        storage=FirebaseStorage.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();


        save_btn = findViewById(R.id.save_btn);
        add_baby = findViewById(R.id.add_baby);
        reg_babyname = findViewById(R.id.reg_babyname);
        baby_birthday = findViewById(R.id.baby_birthday);
        baby_weight = findViewById(R.id.baby_weight);
        baby_gender = findViewById(R.id.baby_gender);
        editText = (EditText) findViewById(R.id.baby_birthday);

        babyUser = new BabyUser();


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(BabyInfoActivity.this, date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String babyName = reg_babyname.getText().toString();
                String babyBirthday = baby_birthday.getText().toString();
                String babyWeight = baby_weight.getText().toString();
                String babyGender = baby_gender.getSelectedItem().toString();

                if(babyName.isEmpty()){
                    reg_babyname.setError("Name required");
                    reg_babyname.requestFocus();
                    return;
                }
                if(babyBirthday.isEmpty()){
                    baby_birthday.setError("Baby birthday required");
                    baby_birthday.requestFocus();
                    return;
                }
                if(babyWeight.isEmpty()){
                    baby_weight.setError("Weight required");
                    baby_weight.requestFocus();
                    return;
                }

//                if (TextUtils.isEmpty(babyName) && TextUtils.isEmpty(babyBirthday) && TextUtils.isEmpty(babyWeight) && TextUtils.isEmpty(babyGender)){
//                    Toast.makeText(BabyInfoActivity.this, "Add some data", Toast.LENGTH_SHORT).show();
//                }else {
//                    addDatatoFirebase(babyName, babyBirthday, babyGender,babyWeight);
//                }
//                reference.setValue(babyUser);


                baby_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String babyGender = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }

        });

                String user_id = auth.getCurrentUser().getUid();
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("babyUsers").child(user_id).child("Info");

                newPost.put("babyName", babyName);
                newPost.put("babyBirthday", babyBirthday);
                newPost.put("babyWeight", babyWeight);
                newPost.put("babyGender", babyGender);

                current_user_db.setValue(newPost);

                Intent r = new Intent(BabyInfoActivity.this, HomeActivity.class);
                startActivity(r);
        }
        });


        add_baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

    }
//
//    private void addDatatoFirebase(String babyName, String babyBirthday, String babyGender, String babyWeight) {
//        babyUser.setBabyName(babyName);
//        babyUser.setBabyBirthday(babyBirthday);
//        babyUser.setBabyGender(babyGender);
//        babyUser.setBabyWeight(babyWeight);
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                reference.setValue(babyUser);
//                Toast.makeText(BabyInfoActivity.this, "Data added", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(BabyInfoActivity.this, "Fail to add data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==10)
        {
            if (data!=null)
            {
                imageUri=data.getData();
                add_baby.setImageURI(imageUri);
            }
        }
    }

        private void updateLabel(){
            String myFormat="MM/dd/yy";
            SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
            editText.setText(dateFormat.format(myCalendar.getTime()));
        }


    }
