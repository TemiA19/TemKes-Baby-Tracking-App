package com.mobileapp.temkesbabytrackingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    TextView txt_signin, signup_btn;
    CircleImageView profile_image;
    EditText reg_name,reg_email,reg_password, reg_cpassword;
    FirebaseAuth auth;
    String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    Uri imageUri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imageURI;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);


        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        txt_signin=findViewById(R.id.txt_signin);
        profile_image=findViewById(R.id.profile_image);
        reg_email=findViewById(R.id.reg_email);
        reg_name=findViewById(R.id.reg_name);
        reg_password=findViewById(R.id.reg_password);
        reg_cpassword=findViewById(R.id.reg_cpassword);
        signup_btn=findViewById(R.id.signup_btn);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(RegistrationActivity.this,HomeActivity.class));

                progressDialog.show();
                String name = reg_name.getText().toString();
                String email = reg_email.getText().toString();
                String password = reg_password.getText().toString();
                String cpassword = reg_cpassword.getText().toString();


                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(cpassword)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    reg_email.setError("Please Enter Valid Email");
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(cpassword)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference reference=database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference=storage.getReference().child("upload").child(auth.getUid());
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                firebaseUser.sendEmailVerification();
                                if (imageUri!=null)
                                {
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful())
                                            {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageURI=uri.toString();
                                                        Users users=new Users(auth.getUid(),name,email,imageURI, password);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful())
                                                                {
                                                                    startActivity(new Intent(RegistrationActivity.this,BabyInfoActivity.class));
                                                                }else {
                                                                    Toast.makeText(RegistrationActivity.this, "Error in creating a new user", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else {
                                    imageURI="https://firebasestorage.googleapis.com/v0/b/temkes-baby-tracking-app.appspot.com/o/profile_image.png?alt=media&token=6ebf7d80-8a8e-4759-b00b-c9ea881e7d23";
                                    Users users=new Users(auth.getUid(),name,email,imageURI,password);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                progressDialog.dismiss();
                                                startActivity(new Intent(RegistrationActivity.this,BabyInfoActivity.class));
                                            }else {
                                                Toast.makeText(RegistrationActivity.this, "Error in creating a new user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==10)
        {
            if (data!=null)
            {
                imageUri=data.getData();
                profile_image.setImageURI(imageUri);
            }
        }
    }
}