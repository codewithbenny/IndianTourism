package com.india.tourism.Guide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.india.tourism.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class GuideSettingsActivity extends AppCompatActivity {
    private CircleImageView profileImageView;
    private EditText fullNameEditText, userPhoneEditText, LanguageEditText,city,place,payscale,experience;
    private TextView profileChangeTextBtn,  closeTextBtn, saveTextButton;
    private String image;
    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";
    private String phonekey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_settings);
        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child(" Tourist Profile pictures");
        profileImageView = (CircleImageView) findViewById(R.id.setting_profile);
        fullNameEditText = (EditText) findViewById(R.id.setting_name);
        userPhoneEditText = (EditText) findViewById(R.id.setting_phone1);
        LanguageEditText = (EditText) findViewById(R.id.setting_Language1);
        profileChangeTextBtn = (TextView) findViewById(R.id.profilechange1);
        city=findViewById(R.id.setting_guide_city);
        place=findViewById(R.id.setting_guide_places);
        payscale=findViewById(R.id.setting_payscale);
        experience=findViewById(R.id.setting_guide_eperience);
        closeTextBtn = (TextView) findViewById(R.id.idclose);
        saveTextButton = (TextView) findViewById(R.id.idupdate);
        SharedPreferences preferences=getApplicationContext().getSharedPreferences("myfile",0);

        phonekey=preferences.getString("guidephn",null);

        userInfoDisplay(profileImageView, fullNameEditText, userPhoneEditText, LanguageEditText,city,place,payscale,experience);

        Paper.init(this);
        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                    finish();


            }
        });


        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                    if (checker.equals("clicked"))
                    {
                        userInfoSaved();
                    }
                    else
                    {
                        updateOnlyUserInfo();
                    }


            }
        });


        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(GuideSettingsActivity.this);
            }
        });
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText languageEditText, final EditText city, final EditText place, final EditText payscale, final EditText experience) {

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("ApprovedGuides").child(phonekey);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("Agimage").exists()) {
                    Toast.makeText(GuideSettingsActivity.this, "Update Your Profile Pic", Toast.LENGTH_SHORT).show();
                     image = dataSnapshot.child("Agimage").getValue().toString();
                    Picasso.get().load(image).into(profileImageView);
                    Paper.book().write("Agimage",image);
                }
                    String name = dataSnapshot.child("Agname").getValue().toString();
                    String phone = dataSnapshot.child("Agphone").getValue().toString();
                    String city1 = dataSnapshot.child("Agcity").getValue().toString();
                    String place1 = dataSnapshot.child("Agplace").getValue().toString();
                    String exp = dataSnapshot.child("Agexperience").getValue().toString();
                    String lan  = dataSnapshot.child("Aglanguage").getValue().toString();
                    String pay = dataSnapshot.child("Apayscale").getValue().toString();

                    fullNameEditText.setText(name);
                    userPhoneEditText.setText(phone);
                    languageEditText.setText(lan);
                    city.setText(city1);
                    experience.setText(exp);
                    place.setText(place1);
                    payscale.setText(pay);
                    experience.setText(exp);





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void updateOnlyUserInfo()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ApprovedGuides");
        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("Agname", fullNameEditText.getText().toString());
        userMap. put("Aglanguage", LanguageEditText.getText().toString());
        userMap. put("Agphone", userPhoneEditText.getText().toString());
        userMap.put("Agpayscale",payscale.getText().toString());
        userMap.put("Agexperience",experience.getText().toString());
        userMap.put("Agcity",city.getText().toString());
        userMap.put("Agplace",place.getText().toString());
        ref.child(phonekey).updateChildren(userMap);

        startActivity(new Intent(GuideSettingsActivity.this, GuideHomeActivity.class));
        Toast.makeText(GuideSettingsActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(GuideSettingsActivity.this, GuideSettingsActivity.class));
            finish();
        }
    }




    private void userInfoSaved()
    {
        if (TextUtils.isEmpty(fullNameEditText.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(LanguageEditText.getText().toString()))
        {
            Toast.makeText(this, "Language is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userPhoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Name is phone is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(place.getText().toString())){
            Toast.makeText(this, "place is mandatory.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(city.getText().toString())){
            Toast.makeText(this, "city is mandatory.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(payscale.getText().toString())){
            Toast.makeText(this, "pay is mandatory.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(experience.getText().toString())){
            Toast.makeText(this, "Experience  is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }



    private void uploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null)
        {
            final StorageReference filepath = storageProfilePrictureRef.child((imageUri.getLastPathSegment())  + ".jpg");
            final UploadTask uploadTask = (UploadTask) filepath.putFile(imageUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(GuideSettingsActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();
                }

            }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return filepath.getDownloadUrl();
                }
            })
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String downloadimageurl=String.valueOf(uri);

                            Toast.makeText(GuideSettingsActivity.this, "get Successfully"+downloadimageurl, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();
                        Toast.makeText(GuideSettingsActivity.this, "task is succesful", Toast.LENGTH_SHORT).show();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ApprovedGuides");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap. put("Agname", fullNameEditText.getText().toString());
                        userMap. put("Alanguage", LanguageEditText.getText().toString());
                        userMap. put("Agphone", userPhoneEditText.getText().toString());
                        userMap.put("Agpayscale",payscale.getText().toString());
                        userMap.put("Agexperience",experience.getText().toString());
                        userMap.put("Agcity",city.getText().toString());
                        userMap.put("Agplace",place.getText().toString());
                        userMap. put("Agimage", myUrl);
                        ref.child(phonekey).updateChildren(userMap);

                        progressDialog.dismiss();
                        if(imageUri==null) {

                            startActivity(new Intent(GuideSettingsActivity.this, GuideHomeActivity.class));
                            Toast.makeText(GuideSettingsActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(GuideSettingsActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GuideSettingsActivity.this, "failure occurs", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Toast.makeText(this, "image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }





}
