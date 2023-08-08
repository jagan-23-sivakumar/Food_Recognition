package com.mastercoding.explicitintentapp;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
public class MainActivity extends AppCompatActivity {
    Button logbtn,signupbtn;
    //Firebase Authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    public FirebaseFirestore db=FirebaseFirestore.getInstance();

    public CollectionReference collectionReference = db.collection("Users");
    EditText logname,logemail,logpass;

    //@SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logbtn=findViewById(R.id.btnlogin);
        signupbtn=findViewById(R.id.button_first);

        //logname=findViewById(R.id.inputlogname);

        logemail=findViewById(R.id.inputlogmail);
        logpass=findViewById(R.id.inputlogname);

        firebaseAuth = FirebaseAuth.getInstance();

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(MainActivity.this,Activity2.class);
                startActivity(i);
            }
        });
        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent dummy=new Intent(MainActivity.this,DummyActivity.class);
                //startActivity(dummy);
                LoginEmailPasswordUser(
                        logemail.getText().toString().trim(),
                        logpass.getText().toString().trim()
                );
            }
        });
    }
    private void LoginEmailPasswordUser(String email, String pwd) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)){
            firebaseAuth.signInWithEmailAndPassword(email,pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            assert user != null;
                            final String currentUserId = user.getUid();

                            collectionReference.
                                    whereEqualTo("userId", currentUserId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                            if (error != null) {

                                            }
                                            assert value != null;
                                            if (!value.isEmpty()) {

                                                for (QueryDocumentSnapshot snapshot : value) {
                                                    startActivity(new Intent(MainActivity.this, Splashscreen.class));
                                                }
                                            }
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // If Failed:
                            Toast.makeText(MainActivity.this,
                                    "Invalid email or password", Toast.LENGTH_LONG).show();
                        }
                    });
        }else{
            Toast.makeText(MainActivity.this,
                    "Please Enter email & password"
                    , Toast.LENGTH_SHORT).show();
        }
    }

}