package com.mobileapp.temkesbabytrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    static final float END_SCALE = 0.7f;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    
    LinearLayout contentView;

    private TextView fullNameInput, emailInput, passwordInput;
    private EditText updateName, updateEmail, updatePassword;
    private String fullName, email, password;
    private String nameUpdate, emailUpdate, passwordUpdate;
    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        
        fullNameInput = findViewById(R.id.full_name_label);
        emailInput = findViewById(R.id.email_field);
        passwordInput = findViewById(R.id.password_label);
        updateName = findViewById(R.id.updateProfileName);
        updateEmail = findViewById(R.id.updateProfileEmail);
        updatePassword = findViewById(R.id.updateProfilePassword);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        
        if (firebaseUser == null){
            Toast.makeText(EditProfileActivity.this, "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();
        }else {
            showUserProfile(firebaseUser);
        }


        Button buttonUpdate = findViewById(R.id.updateBtn);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(firebaseUser);
            }
        });
        navigationDrawer();
        contentView=findViewById(R.id.content_profile);


    }

    private void updateProfile(FirebaseUser firebaseUser){
        String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (TextUtils.isEmpty(nameUpdate) || TextUtils.isEmpty(emailUpdate) ||
                TextUtils.isEmpty(passwordUpdate)){
            Toast.makeText(EditProfileActivity.this, "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
        } else if (!emailUpdate.matches(emailPattern)) {
            updateEmail.setError("Please Enter Valid Email");
            Toast.makeText(EditProfileActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
        } else {
            nameUpdate = updateName.getText().toString();
            emailUpdate = updateEmail.getText().toString();
            passwordUpdate = updatePassword.getText().toString();

            Users users = new Users(nameUpdate, emailUpdate, passwordUpdate);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

            String userID = firebaseUser.getUid();

            reference.child(userID).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().
                                setDisplayName(nameUpdate).build();
                        firebaseUser.updateProfile(profileChangeRequest);

                        Toast.makeText(EditProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProfileActivity.this, EditProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else {
                        try{
                            throw task.getException();
                        } catch (Exception e) {
                            Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

        }
    }


    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //Extracting User Reference from Database for "Registered Users"
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                if (users != null){
                    fullName = users.name;
                    email = firebaseUser.getEmail();
                    password = users.password;

//                    nameUpdate = users.name;
//                    emailUpdate = firebaseUser.getEmail();
//                    passwordUpdate = users.password;

                    fullNameInput.setText(fullName);
                    emailInput.setText(email);
                    passwordInput.setText(password);

//                    updateName.setText(nameUpdate);
//                    updateEmail.setText(emailUpdate);
//                    updatePassword.setText(passwordUpdate);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();

    }

    private void animateNavigationDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent_home = new Intent(this, HomeActivity.class);
                startActivity(intent_home);
                break;
            case R.id.summary:
                Intent intent_summary = new Intent(this, SummaryActivity.class);
                startActivity(intent_summary);
                break;
//            case R.id.chart:
//                Intent intent_chart = new Intent(this, ChartActivity.class);
//                startActivity(intent_chart);
//                break;
            case R.id.edit_profile:
                Intent intent_profile = new Intent(this, EditProfileActivity.class);
                startActivity(intent_profile);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            default:
                return true;
        }
        return true;
    }
}