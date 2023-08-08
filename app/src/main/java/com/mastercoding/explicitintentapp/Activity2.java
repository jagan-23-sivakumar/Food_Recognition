package com.mastercoding.explicitintentapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mastercoding.explicitintentapp.Model.GoalCalorieModel;
import com.mastercoding.explicitintentapp.Model.PredictedresultModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
public class Activity2 extends AppCompatActivity {
    EditText regname,regemail,regpass,regconpass;
    Button regactivity,logback;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    public FirebaseFirestore db=FirebaseFirestore.getInstance();

    public CollectionReference collectionReference=db.collection("Users");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        regactivity=findViewById(R.id.btnRegister);
        logback=findViewById(R.id.button_second);

        regname=findViewById(R.id.inputUsername);
        regemail=findViewById(R.id.inputregmail);
        regpass=findViewById(R.id.inputregpass);
        //regconpass=findViewById(R.id.inputConformPassword);

        firebaseAuth=FirebaseAuth.getInstance();

        logback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logback=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(logback);
            }
        });
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser=firebaseAuth.getCurrentUser();
                if(currentUser!=null){

                }else{

                }
            }
        };
        // Just for page shuffling
        /*regactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent campg=new Intent(Activity2.this,Health_detail.class);
                startActivity(campg);
            }
        });*/
        regactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(regemail.getText().toString()) &&
                        !TextUtils.isEmpty(regname.getText().toString()) &&
                        !TextUtils.isEmpty(regpass.getText().toString())
                        ){

                    String username=regname.getText().toString().trim();
                    String email=regemail.getText().toString().trim();
                    String pass=regpass.getText().toString().trim();
                    //String conpass=regconpass.getText().toString().trim();

                    CreateUser(username,email,pass);

                }else{
                    Toast.makeText(Activity2.this, "Empty fields are detected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void CreateUser(final String username, String email, String pass) {

        if(!TextUtils.isEmpty(regemail.getText().toString()) &&
                !TextUtils.isEmpty(regname.getText().toString()) &&
                !TextUtils.isEmpty(regpass.getText().toString())
                ){

            firebaseAuth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                String currentUserId = currentUser.getUid();

                                // Create a userMap so we can create a user in the User Collection in Firestore

                                Map<String, String> userObj = new HashMap<>();
                                userObj.put("userId", currentUserId);
                                userObj.put("username", username);

                                //Adding Users to Firestore
                                collectionReference.add(userObj)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (Objects.requireNonNull(task.getResult()).exists()) {
                                                                    String name = task.getResult().getString("username");
                                                                    PredictedresultModel predictedresultModel2=new PredictedresultModel();
                                                                    //predictedresultModel2.setGoalcalorie("0");
                                                                    db.collection("Detailedresults").document(currentUserId).set(predictedresultModel2);
                                                                    GoalCalorieModel goalCalorieModel3=new GoalCalorieModel();
                                                                    //goalCalorieModel3.setGoalCalorie("0");
                                                                    db.collection("GoalCalorie").document(currentUserId).set(goalCalorieModel3);
                                                                    Intent i = new Intent(Activity2.this,
                                                                            Health_detail.class);

                                                                    i.putExtra("username", name);
                                                                    i.putExtra("userId", currentUserId);
                                                                    startActivity(i);
                                                                } else {
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                // Display Failed Message
                                                                Toast.makeText(Activity2.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        });
                            }
                        }
                    });
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
