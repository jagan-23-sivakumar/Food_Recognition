package com.mastercoding.explicitintentapp;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mastercoding.explicitintentapp.Model.CheatModel;
import com.mastercoding.explicitintentapp.Model.GoalCalorieModel;
import com.mastercoding.explicitintentapp.Model.PredictedresultModel;
import com.mastercoding.explicitintentapp.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class DummyActivity extends AppCompatActivity {
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private DocumentReference userId;

    //double totalCalorie=0.0;
    Button select, capture, predict,setgoalcal;
    TextView result,predictedcalorie;
    Bitmap image;
    Button sugesstion;
    ImageView imageView;
    int imageSize = 224;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    EditText inputcal;

    ProgressDialog progressDialog;
    String goalcalstore;
    public PredictedresultModel predictedresultModel=new PredictedresultModel();
    String currentname;

    PredictedresultModel  predictedresultModel1=new PredictedresultModel();

    Date currentdate=new Date();
    int currentTime=currentdate.getHours();
    int currentminute=currentdate.getMinutes();
    int count=0;
    GoalCalorieModel goalCalorieModel=new GoalCalorieModel();

    //ArrayList<String> allfoodconsumed=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        //permission
        select = findViewById(R.id.button);
        capture = findViewById(R.id.button2);
        predict = findViewById(R.id.button3);
        imageView = findViewById(R.id.imageView);
        // liveahealth=findViewById(R.id.textView3);
        result = findViewById(R.id.textView2);
        firebaseAuth = FirebaseAuth.getInstance();
        inputcal=findViewById(R.id.editTextText2);
        setgoalcal=findViewById(R.id.button7);
        //Calendar calendar=Calendar.getInstance();

        sugesstion = findViewById(R.id.button4);
        predictedcalorie=findViewById(R.id.textView5);
       /*if (currentTime==20){
           FirebaseUser user = firebaseAuth.getCurrentUser();
           assert user != null;
           final String currentUserId = user.getUid();
           PredictedresultModel predictedresultModel2=new PredictedresultModel();
           db.collection("Detailedresults").document(currentUserId).set(predictedresultModel2);
       }*/


       sugesstion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DummyActivity.this,OutputActivity.class));
            }
       });

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog=new ProgressDialog(DummyActivity.this);
                progressDialog.setTitle("Predicting....");
                progressDialog.show();

                /*FirebaseUser user = firebaseAuth.getCurrentUser();
                assert user != null;
                final String currentUserId = user.getUid();
                userId=db.collection("Users").document(currentUserId);
                userId.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){
                                    String calorie=documentSnapshot.getString("calorie");
                                    result.setText("Goal Calorie : "+calorie+ "\nRemaining Calorie :"+calorie);
                                }
                            }
                        });*/
                //classifyImage(image);

                FirebaseUser user = firebaseAuth.getCurrentUser();
                assert user != null;
                final String currentUserId = user.getUid();

                db.collection("GoalCalorie").document(currentUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String outputname=classifyImage(image);
                            currentname=outputname;
                            predictedresultModel.setName(outputname.trim());
                            String finalgoalcal=documentSnapshot.getString("goalCalorie");
                            predictedresultModel.setGoalcalorie(finalgoalcal);
                            //String outputname="halwa";
                            findCalorie(outputname.trim());

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });

        setgoalcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                assert user != null;
                final String currentUserId = user.getUid();

                goalcalstore=inputcal.getText().toString().trim();
                //predictedresultModel.setGoalcalorie(goalcalstore);
                goalCalorieModel.setGoalCalorie(goalcalstore);
                db.collection("GoalCalorie").document(currentUserId).set(goalCalorieModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        inputcal.setText("");
                        inputcal.clearFocus();

                    }
                });
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckPermission()) {
                    captureImage();
                } else {
                    RequestPermission();
                }
            }
        });
    }
    private void findCalorie(String outputname) {
        CollectionReference collectionReference = db.collection("cheating");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snapshots : queryDocumentSnapshots) {
                    CheatModel cheatModel = snapshots.toObject(CheatModel.class);
                    if (cheatModel.getNamedocument().equals(outputname)) {
                        result.setText("Food name: " + outputname);
                        String currentcalorie = cheatModel.getNamecalorie();
                        predictedresultModel.setCalorie(currentcalorie);

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            final String currentUserId = user.getUid();
                            db.collection("Detailedresults").document(currentUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        String refreshtotal = documentSnapshot.getString("totalcalorie");
                                        if (refreshtotal !=null) {
                                            double totalCalorie = Double.parseDouble(predictedresultModel.getCalorie()) + Double.parseDouble(refreshtotal);
                                            int inttotalcalorie = (int) totalCalorie;
                                            /*if (currentTime == 20 && currentminute==53) {
                                                // this is the correct refreshing code
                                                double totalCalorielast = Double.parseDouble(predictedresultModel.getCalorie());
                                                predictedresultModel.setTotalcalorie(String.valueOf(totalCalorielast));
                                            }else{*/
                                                predictedresultModel.setTotalcalorie(String.valueOf(inttotalcalorie));
                                        } else {
                                            double totalCalorie =Double.parseDouble(predictedresultModel.getCalorie());
                                            int inttotalcalorie = (int) totalCalorie;
                                            /*if (currentTime == 20 && currentminute==54) {
                                                // this is the correct refreshing code
                                                double totalCalorielast = Double.parseDouble(predictedresultModel.getCalorie());
                                                predictedresultModel.setTotalcalorie(String.valueOf(totalCalorielast));
                                            }else{*/
                                                predictedresultModel.setTotalcalorie(String.valueOf(inttotalcalorie));
                                        }
                                        predictedresultModel.setRemcalorie(String.valueOf((int) (Double.parseDouble(predictedresultModel.getGoalcalorie()) -
                                                Double.parseDouble(predictedresultModel.getTotalcalorie()))));
                                        predictedcalorie.setText("Predicted Calorie: " + cheatModel.getNamecalorie() + " kcal");
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        assert user != null;
                                        final String currentUserId = user.getUid();
                                        predictedresultModel.setId(currentUserId);

                                        db.collection("Detailedresults").document(currentUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    String goalcalnew = documentSnapshot.getString("goalcalorie");
                                                    goalcalstore = goalcalnew;
                                                    predictedresultModel.setGoalcalorie(goalcalstore);
                                                }
                                            }
                                        });

                                        db.collection("Detailedresults").document(currentUserId).set(predictedresultModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //Toast.makeText(DummyActivity.this, "Sucessfully added", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //Toast.makeText(DummyActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }

                                    }


                                }
                            });

                    }
                }
            }

            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(DummyActivity.this, "Retry Something wrong", Toast.LENGTH_SHORT).show();
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }

    //permission method
    private boolean CheckPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA_SERVICE);
        return cameraPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermission() {
        int PERMISSION_CODE = 200;
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA}, PERMISSION_CODE);
    }

    private void captureImage() {
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takepic.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takepic, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (cameraPermission) {
                //Toast.makeText(this, "Permission Granted ", Toast.LENGTH_SHORT).show();
                captureImage();
            } else {
                Toast.makeText(getApplicationContext(), "Permission denied !", Toast.LENGTH_SHORT).show();
            }
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
                Intent i = new Intent(DummyActivity.this, your_profile.class);
                startActivity(i);
                break;

            case R.id.signout:
                // signout code
                //if(currentUser!=null && firebaseAuth!=null){
                firebaseAuth.signOut();
                startActivity(new Intent(DummyActivity.this, MainActivity.class));
                //}
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public String classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(getApplicationContext());

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for(int i = 0; i < imageSize; i ++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);
            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            //String[] classes = {"paniyaram","dhokla","kathiroll","upma","dosa","poori","gulabjamun", "butternaan","vadapav","tandoorichicken","venpongal","chappati"
            //,"briyani","bisibelebath","noodles","idly","samosa","halwa","meduvadai","chaat"};

            String[] classes = {"bisibelebath","briyani","butternaan","dhokla","chappati","chaat",
                    "dosa", "gulabjamun","halwa","idly",
                    "kathiroll","methuvadai","noodles","paniyaram","poori","samosa",
                    "tandoorichicken","upma","vadapav","venpongal"};
            //result.setText(classes[maxPos]);
            // Releases model resources if no longer used.
            model.close();
            return classes[maxPos];
        } catch (IOException e) {
            // TODO Handle the exception
        }
        return "Failed try again";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            image = (Bitmap) extras.get("data");
            //imageView.setImageBitmap((bitmap));
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            imageView.setImageBitmap(image);
            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            //classifyImage(image);
        }else if(requestCode==10){
            if(data!=null){
                Uri uri=data.getData();
                try {
                    image= MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                    int dimension = Math.min(image.getWidth(), image.getHeight());
                    image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                    imageView.setImageBitmap(image);
                    image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                    //classifyImage(image);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}