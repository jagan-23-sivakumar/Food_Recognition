package com.mastercoding.explicitintentapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mastercoding.explicitintentapp.Model.CheatModel;
import com.mastercoding.explicitintentapp.Model.GoalCalorieModel;
import com.mastercoding.explicitintentapp.Model.PredictedresultModel;

import java.util.ArrayList;
public class OutputActivity extends AppCompatActivity {
    Button btn;
    CircularProgressIndicator indicator;

    LinearProgressIndicator linearProgressIndicator;
    EditText changecalorie;
    Button modifycalorie,reset;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    //PredictedresultModel predictedresultModel = new PredictedresultModel();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private DocumentReference userId;
    TextView totcalcirc,goalcalcirc,remgoal,remcal;
    ArrayList<PredictedresultModel> storedata;
    GoalCalorieModel goalCalorieModel=new GoalCalorieModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);
        btn = findViewById(R.id.button1);
        indicator = findViewById(R.id.circularProgressIndicator);
        //calorie=cm.getNamecalorie();
        firebaseAuth = FirebaseAuth.getInstance();
        changecalorie = findViewById(R.id.editTextText);
        modifycalorie = findViewById(R.id.button5);
        totcalcirc = findViewById(R.id.textView);
        goalcalcirc = findViewById(R.id.textView2);
        storedata = new ArrayList<>();
        remcal = findViewById(R.id.textView6);
        remgoal = findViewById(R.id.textView7);
        reset = findViewById(R.id.button6);


        linearProgressIndicator = findViewById(R.id.linearProgressIndicator2);


        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        final String currentUserId = user.getUid();

        db.collection("GoalCalorie").document(currentUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String updatedgoal=documentSnapshot.getString("goalCalorie");
                    db.collection("Detailedresults").document(currentUserId)
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    PredictedresultModel predictedresultModel1 = new PredictedresultModel();
                                    String calorie = documentSnapshot.getString("calorie");
                                    String goalcalorie = documentSnapshot.getString("goalcalorie");
                                    String id = documentSnapshot.getString("id");
                                    String name = documentSnapshot.getString("name");
                                    String remcalorie = documentSnapshot.getString("remcalorie");
                                    String totalcalorie = documentSnapshot.getString("totalcalorie");
                                    predictedresultModel1.setCalorie(calorie);
                                    predictedresultModel1.setGoalcalorie(updatedgoal);
                                    predictedresultModel1.setId(id);
                                    //predictedresultModel.setGoalcalorie(goalcalorie);
                                    predictedresultModel1.setTotalcalorie(totalcalorie);
                                    predictedresultModel1.setName(name);
                                    predictedresultModel1.setRemcalorie(String.valueOf((int) (-Double.parseDouble(predictedresultModel1.getTotalcalorie()) +
                                            Double.parseDouble(predictedresultModel1.getGoalcalorie()))));


                                    goalcalcirc.setText("of " + predictedresultModel1.getGoalcalorie() + " kcal");
                                    totcalcirc.setText(predictedresultModel1.getTotalcalorie());
                                    remcal.setText("   " + predictedresultModel1.getRemcalorie());
                                    remgoal.setText("of " + predictedresultModel1.getGoalcalorie() + " kcal");
                                    double percent = Double.parseDouble(predictedresultModel1.getTotalcalorie())
                                            / Double.parseDouble(predictedresultModel1.getGoalcalorie());
                                    int percentint = (int) (percent * 100);
                                    indicator.setProgress(percentint);
                                    linearProgressIndicator.setProgress(100 - percentint);
                                }
                            }
                        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                //Toast.makeText(OutputActivity.this, "Total calorie added", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Toast.makeText(OutputActivity.this, "Something wrong in total calorie", Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        //    }
        //});

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OutputActivity.this, SugesstionPage.class);
                startActivity(i);
            }
        });

        modifycalorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String goalcal = changecalorie.getText().toString().trim();
                Log.v("TAGY", goalcal);
                FirebaseUser user = firebaseAuth.getCurrentUser();
                assert user != null;
                final String currentUserId = user.getUid();

                userId = db.collection("Detailedresults").document(currentUserId);
                userId.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    PredictedresultModel predictedresultModel = new PredictedresultModel();
                                    String calorie = documentSnapshot.getString("calorie");
                                    String goalcalorie = documentSnapshot.getString("goalcalorie");
                                    String id = documentSnapshot.getString("id");
                                    String name = documentSnapshot.getString("name");
                                    String remcalorie = documentSnapshot.getString("remcalorie");
                                    String totalcalorie = documentSnapshot.getString("totalcalorie");
                                    predictedresultModel.setCalorie(calorie);
                                    predictedresultModel.setGoalcalorie(goalcalorie);
                                    predictedresultModel.setId(id);
                                    predictedresultModel.setGoalcalorie(goalcal);
                                    predictedresultModel.setTotalcalorie(totalcalorie);
                                    predictedresultModel.setName(name);
                                    predictedresultModel.setRemcalorie(String.valueOf((int) (-Double.parseDouble(predictedresultModel.getTotalcalorie()) + Double.parseDouble(predictedresultModel.getGoalcalorie()))));
                                    goalcalcirc.setText("of " + predictedresultModel.getGoalcalorie() + " kcal");
                                    totcalcirc.setText(predictedresultModel.getTotalcalorie());
                                    storedata.add(predictedresultModel);
                                    remcal.setText("   " + predictedresultModel.getRemcalorie());
                                    remgoal.setText("of " + predictedresultModel.getGoalcalorie() + " kcal");
                                    double percent = Double.parseDouble(predictedresultModel.getTotalcalorie())
                                            / Double.parseDouble(predictedresultModel.getGoalcalorie());
                                    int percentint = (int) (percent * 100);
                                    indicator.setProgress(percentint);
                                    linearProgressIndicator.setProgress(100 - percentint);


                                    /*db.collection("DetailedResultsoriginal").document(currentUserId).set(predictedresultModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(OutputActivity.this, "Succesfully added", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(OutputActivity.this, "Failed to add", Toast.LENGTH_SHORT).show();
                                        }
                                    });*/

                                    goalCalorieModel.setGoalCalorie(predictedresultModel.getGoalcalorie());
                                    db.collection("GoalCalorie").document(currentUserId).set(goalCalorieModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            changecalorie.setText("");
                                            changecalorie.clearFocus();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                    db.collection("Detailedresults").document(currentUserId).set(predictedresultModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            Toast.makeText(OutputActivity.this, "Succesfully updated the calorie", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //Toast.makeText(OutputActivity.this, "Failed to add", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });

            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                assert user != null;
                final String currentUserId = user.getUid();

                userId = db.collection("Detailedresults").document(currentUserId);
                userId.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    PredictedresultModel predictedresultModel = new PredictedresultModel();
                                    String calorie = documentSnapshot.getString("calorie");
                                    String goalcalorie = documentSnapshot.getString("goalcalorie");
                                    String id = documentSnapshot.getString("id");
                                    String name = documentSnapshot.getString("name");
                                    String remcalorie = documentSnapshot.getString("remcalorie");
                                    String totalcalorie = documentSnapshot.getString("totalcalorie");
                                    predictedresultModel.setCalorie(calorie);
                                    predictedresultModel.setGoalcalorie(goalcalorie);
                                    predictedresultModel.setId(id);
                                    //predictedresultModel.setGoalcalorie(goalcal);
                                    int totalcal=0;
                                    predictedresultModel.setTotalcalorie(String.valueOf(totalcal));
                                    predictedresultModel.setName(name);
                                    predictedresultModel.setRemcalorie(String.valueOf((int) (-Double.parseDouble(predictedresultModel.getTotalcalorie()) + Double.parseDouble(predictedresultModel.getGoalcalorie()))));
                                    goalcalcirc.setText("of " + predictedresultModel.getGoalcalorie() + " kcal");
                                    totcalcirc.setText(predictedresultModel.getTotalcalorie());
                                    storedata.add(predictedresultModel);
                                    remcal.setText("   " + predictedresultModel.getRemcalorie());
                                    remgoal.setText("of " + predictedresultModel.getGoalcalorie() + " kcal");
                                    double percent = Double.parseDouble(predictedresultModel.getTotalcalorie())
                                            / Double.parseDouble(predictedresultModel.getGoalcalorie());
                                    int percentint = (int) (percent * 100);
                                    indicator.setProgress(percentint);
                                    linearProgressIndicator.setProgress(100 - percentint);


                                    /*db.collection("DetailedResultsoriginal").document(currentUserId).set(predictedresultModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(OutputActivity.this, "Succesfully added", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(OutputActivity.this, "Failed to add", Toast.LENGTH_SHORT).show();
                                        }
                                    });*/

                                    /*goalCalorieModel.setGoalCalorie(predictedresultModel.getGoalcalorie());
                                    db.collection("GoalCalorie").document(currentUserId).set(goalCalorieModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    });*/

                                    db.collection("Detailedresults").document(currentUserId).set(predictedresultModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(OutputActivity.this, "Succesfully reset", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(OutputActivity.this, "Failed to add", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
            }
        });
    }
    //PredictedresultModel predictedresult=storedata.get(0);
    //totcalcirc.setText(predictedresult.getTotalcalorie());
    //goalcalcirc.setText(predictedresult.getGoalcalorie());

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
                Intent i = new Intent(OutputActivity.this, your_profile.class);
                startActivity(i);
                break;

            case R.id.signout:
                // signout code
                //if(currentUser!=null && firebaseAuth!=null){
                firebaseAuth.signOut();
                startActivity(new Intent(OutputActivity.this,MainActivity.class));
                //}

                //break;

        }
        return super.onOptionsItemSelected(item);
    }
}
