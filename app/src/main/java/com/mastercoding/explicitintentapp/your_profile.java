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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mastercoding.explicitintentapp.Model.PredictedresultModel;

import java.util.HashMap;
import java.util.Map;
public class your_profile extends AppCompatActivity {
    Button upcal, updet, calcalorie, profyour;
    TextView pname, page, pweight, pheight, pgender, pcalorie;
    EditText changecal;
    public static final String KEY_CALORIE = "calorie";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_AGE = "age";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private DocumentReference userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_your_profile);
        profyour = findViewById(R.id.yourprof);

        updet = findViewById(R.id.btnupdatedetails);
        calcalorie = findViewById(R.id.btncalculatecalorie);

        pname = findViewById(R.id.profname);
        page = findViewById(R.id.profage);
        pweight = findViewById(R.id.profweight);
        pheight = findViewById(R.id.profheight);
        pgender = findViewById(R.id.profgender);
        pcalorie = findViewById(R.id.profcalorie);

        firebaseAuth = FirebaseAuth.getInstance();


        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        final String currentUserId = user.getUid();


        userId = db.collection("Users").document(currentUserId);
                userId = db.collection("Users").document(currentUserId);
                userId.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override

                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    //String calorie = documentSnapshot.getString(KEY_CALORIE);
                                    String gender = documentSnapshot.getString(KEY_GENDER);
                                    String name = documentSnapshot.getString(KEY_FULLNAME);
                                    String age = documentSnapshot.getString(KEY_AGE);
                                    String weight = documentSnapshot.getString(KEY_WEIGHT);
                                    String height = documentSnapshot.getString(KEY_HEIGHT);

                                    pname.setText("Name : " + name);
                                    page.setText("Age : " + age);
                                    pweight.setText("Weight : " + weight);
                                    pheight.setText("Height : " + height);
                                    pgender.setText("Gender : " + gender);
                                    //pcalorie.setText("Goal Calorie : " + calorie);
                                }
                            }
                        });
           // }
        //});

        updet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updet = new Intent(your_profile.this, Health_detail.class);
                startActivity(updet);
            }
        });

        calcalorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent campage = new Intent(your_profile.this, DummyActivity.class);
                startActivity(campage);
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signout:
                // signout code
                //if(currentUser!=null && firebaseAuth!=null){
                firebaseAuth.signOut();
                startActivity(new Intent(your_profile.this,MainActivity.class));
                //}
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

