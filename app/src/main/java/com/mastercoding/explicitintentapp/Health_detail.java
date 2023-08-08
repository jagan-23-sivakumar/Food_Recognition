package com.mastercoding.explicitintentapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
public class Health_detail extends AppCompatActivity {
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String KEY_CALORIE = "calorie";
    public static final String KEY_FULLNAME="fullname";
    public static final String KEY_GENDER="gender";
    public static final String KEY_WEIGHT="weight";
    public static final String KEY_HEIGHT="height";
    public static final String KEY_AGE="age";
    EditText fullnameinfo,genderinfo, weightinfo, ageinfo, heightinfo;
    Button mainpage;
    CheckBox approve;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_detail);
        mainpage = findViewById(R.id.button_submit);
        fullnameinfo=findViewById(R.id.fullname);
        genderinfo = findViewById(R.id.gender);
        weightinfo = findViewById(R.id.current_weight);
        ageinfo = findViewById(R.id.age);
        heightinfo = findViewById(R.id.height);
        //calorieinfo = findViewById(R.id.prescribedcalorie);
        approve=findViewById(R.id.conditions);

        firebaseAuth = FirebaseAuth.getInstance();

        mainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(approve.isChecked()){
                    saveDatatofirestore();
                }else{
                    Toast.makeText(Health_detail.this, "Please Accept the terms and conditions ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void saveDatatofirestore() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        final String currentUserId = user.getUid();

        if(!TextUtils.isEmpty(genderinfo.getText().toString()) &&
                !TextUtils.isEmpty(weightinfo.getText().toString()) &&
                !TextUtils.isEmpty(ageinfo.getText().toString()) &&
                !TextUtils.isEmpty(heightinfo.getText().toString()) &&
                 !TextUtils.isEmpty(fullnameinfo.getText().toString())) {

            //String calorie = calorieinfo.getText().toString().trim();
            String fullname = fullnameinfo.getText().toString().trim();
            String age = ageinfo.getText().toString().trim();
            String weight = weightinfo.getText().toString().trim();
            String height = heightinfo.getText().toString().trim();
            String gender = genderinfo.getText().toString().trim();

            Map<String, Object> data = new HashMap<>();
            data.put(KEY_FULLNAME, fullname);
            data.put(KEY_AGE, age);
            data.put(KEY_WEIGHT, weight);
            data.put(KEY_HEIGHT, height);
            data.put(KEY_GENDER, gender);
            //data.put(KEY_CALORIE, calorie);
            data.put("id",currentUserId);

            //final String userfinalname=user.getDisplayName();
//            Map<String ,Object> data2=new HashMap<>();
//            data.put("name",userfinalname);
//            data.put("id",currentUserId);
            //db.collection("current").document("checking")
            //final String currentusername=user

            db.collection("Users").document(currentUserId)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Health_detail.this, "Successfully added", Toast.LENGTH_SHORT).show();
                            Intent campage = new Intent(Health_detail.this, DummyActivity.class);
                            startActivity(campage);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Health_detail.this, "Failed!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(Health_detail.this, "Empty fields found  !!!", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                //go to your_progfile activity
                Intent i = new Intent(Health_detail.this, your_profile.class);
                startActivity(i);
                break;

            case R.id.signout:
                // signout code
                //if(currentUser!=null && firebaseAuth!=null){
                    firebaseAuth.signOut();
                    startActivity(new Intent(Health_detail.this,MainActivity.class));
                //}

                break;

        }
        return super.onOptionsItemSelected(item);
    }
}