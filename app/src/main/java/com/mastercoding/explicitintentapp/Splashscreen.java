package com.mastercoding.explicitintentapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;
public class Splashscreen extends AppCompatActivity {
    Button btn;
    TextView t1, t2;
    Animation animate_btn, animate_txt;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    public FirebaseFirestore db=FirebaseFirestore.getInstance();

    public CollectionReference collectionReference = db.collection("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        firebaseAuth=FirebaseAuth.getInstance();
        btn = findViewById(R.id.buttonsplash);
        t1 = findViewById(R.id.textViewsplash);
        t2 = findViewById(R.id.textView2splash);

        // Now let's add the animation
        animate_btn = AnimationUtils.loadAnimation(this,
                R.anim.animate_btn);

        animate_txt = AnimationUtils.loadAnimation(this,
                R.anim.animate_texts);

        //btn.setBackgroundColor(Color.RED);
        btn.setAnimation(animate_btn);

        // Let's Create animation for the text

        t1.setAnimation(animate_txt);
        t2.setAnimation(animate_txt);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Splashscreen.this, DummyActivity.class);
                startActivity(i);
            }
        });
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
                Intent i = new Intent(Splashscreen.this, your_profile.class);
                startActivity(i);
                break;

            case R.id.signout:
                // signout code
                //if(currentUser!=null && firebaseAuth!=null){
                    firebaseAuth.signOut();
                    startActivity(new Intent(Splashscreen.this,MainActivity.class));
                //}

                //break;

        }
        return super.onOptionsItemSelected(item);
    }
}
