package com.mastercoding.explicitintentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mastercoding.explicitintentapp.Adapter.SugesstionAdapter;
import com.mastercoding.explicitintentapp.Model.SugessstionModel;

import java.util.ArrayList;

public class SugesstionPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SugesstionAdapter adapter;
    private ArrayList<SugessstionModel> suggesstionModelArray;
    int count=0;
    EditText type;
    Button btn;
    String patienttype;
    ProgressDialog progressDialog;
    public FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth;
    public CollectionReference collectionReference ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sugesstion_page);
        type=findViewById(R.id.categorypatient);
        btn=findViewById(R.id.fetcher);
        recyclerView = findViewById(R.id.normal_recyc);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        suggesstionModelArray = new ArrayList<>();
        adapter = new SugesstionAdapter(this, suggesstionModelArray);
        recyclerView.setAdapter(adapter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog=new ProgressDialog(SugesstionPage.this);
                progressDialog.setTitle("Fetching Suggestions.....");
                progressDialog.show();
                getAllDocumentsCollection();
                type.setText("");
                type.clearFocus();
            }
        });
    }
    private void getAllDocumentsCollection() {
        patienttype = type.getText().toString().trim();
        collectionReference = db.collection(patienttype);

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (count == 0) {
                    for (QueryDocumentSnapshot snapshots : queryDocumentSnapshots) {
                        SugessstionModel sugessstionModel = snapshots.toObject(SugessstionModel.class);
                        suggesstionModelArray.add(sugessstionModel);
                        Log.v("TAGY", sugessstionModel.getName());
                        Log.v("TAGY", sugessstionModel.getCalorie());
                        Log.v("TAGY", sugessstionModel.getImage());
                    }
                    Toast.makeText(SugesstionPage.this, "Sucessfully retrived", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    count++;
                    adapter.notifyDataSetChanged();

                } else if (count > 0) {
                    suggesstionModelArray.clear();
                    for (QueryDocumentSnapshot snapshots : queryDocumentSnapshots) {
                        SugessstionModel sugessstionModel = snapshots.toObject(SugessstionModel.class);
                        suggesstionModelArray.add(sugessstionModel);
                    }
                    Toast.makeText(SugesstionPage.this, "Sucessfully retrived", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    count++;
                    adapter.notifyDataSetChanged();

                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SugesstionPage.this, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
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
                Intent i = new Intent(SugesstionPage.this, your_profile.class);
                startActivity(i);
                break;

            case R.id.signout:
                // signout code
                //if(currentUser!=null && firebaseAuth!=null){
                firebaseAuth.signOut();
                startActivity(new Intent(SugesstionPage.this,MainActivity.class));
                //}

                //break;

        }
        return super.onOptionsItemSelected(item);
    }
}