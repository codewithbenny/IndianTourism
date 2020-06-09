package com.india.tourism.Guide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.UploadTask;
import com.india.tourism.Model.Places;
import com.india.tourism.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddnewplaceActivity extends AppCompatActivity {
    private String abc, Pname, url1, savecurrentdate, savecurrenttime, Fulldes, ProductRandomkey, downloadimageurl, Placefees, Placetimings;
    private ImageView placeimage;
    private EditText placename, placedescription, placefees, placetimings, fuldes, url;
    private Button addproduct, approve;
    private static final int GalleryPick = 1;
    private Uri Imageuri;
    private StorageReference ImageRef;
    private DatabaseReference Placesref;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_addnewplace);
        placeimage = findViewById(R.id.Select_Place_image);
        placename = findViewById(R.id.place_name);
        addproduct = findViewById(R.id.addnewplace);

        url = findViewById(R.id.place_url);
        approve = findViewById(R.id.approve);
        placefees = findViewById(R.id.place_entryfee);
        placetimings = findViewById(R.id.place_timings);
        loadingbar = new ProgressDialog(this);

        ImageRef = FirebaseStorage.getInstance().getReference().child("Places Images");
        Placesref = FirebaseDatabase.getInstance().getReference().child("Places");
//        approve.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                abc = "update";
//                Pname = placename.getText().toString();
//                url1 = url.getText().toString();
//                Placetimings = placetimings.getText().toString();
//                Placefees = placefees.getText().toString();
//                Toast.makeText(AdminAddnewplaceActivity.this, abc, Toast.LENGTH_SHORT).show();
//                Storeimageinformation();
//
//            }
//        });
        placeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidatePlacedata();
            }
        });

    }


    private void ValidatePlacedata() {

        Pname = placename.getText().toString();
        url1 = url.getText().toString();
        Placetimings = placetimings.getText().toString();
        Placefees = placefees.getText().toString();


        if (Imageuri == null) {
            Toast.makeText(this, "Insert Place Image", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Pname)) {
            Toast.makeText(this, "Enter Place Name", Toast.LENGTH_SHORT).show();
        }
//        else if (TextUtils.isEmpty(Description)) {
//            Toast.makeText(this, "Enter Place Description", Toast.LENGTH_SHORT).show();
//
//        }
        else if (TextUtils.isEmpty(Placefees)) {
            Toast.makeText(this, "Enter Place fees", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Placetimings)) {
            Toast.makeText(this, "Enter Place timings", Toast.LENGTH_SHORT).show();
        } else {
            Storeimageinformation();
        }

    }

    private void Storeimageinformation() {
        loadingbar.setTitle("Adding New Place");
        loadingbar.setMessage("Please Wait while we are adding the new Product");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, yyyy");
        savecurrentdate = currentdate.format(calendar.getTime());
        SimpleDateFormat currentdatetime = new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currentdatetime.format(calendar.getTime());
        ProductRandomkey = savecurrentdate + savecurrenttime;
//        if (Imageuri == null) {
//            updateplace(Pname);
//        } else {
            final StorageReference filepath = ImageRef.child((Imageuri.getLastPathSegment()) + Pname + ".jpg");
            final UploadTask uploadTask = (UploadTask) filepath.putFile(Imageuri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    loadingbar.dismiss();
                    Toast.makeText(AdminAddnewplaceActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();
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

                            downloadimageurl = String.valueOf(uri);

                            Saveproductinfotodatabase();
                            Toast.makeText(AdminAddnewplaceActivity.this, "get Successfully" + downloadimageurl, Toast.LENGTH_SHORT).show();
                        }
                    });
        }



    private void Saveproductinfotodatabase() {
        HashMap<String, Object> placemap = new HashMap<>();
        placemap.put("pid", Pname);
        placemap.put("Name", Pname);
        placemap.put("date", savecurrentdate);
        placemap.put("Timings", Placetimings);
        placemap.put("EntryFee", Placefees);
        placemap.put("Img", downloadimageurl);
        placemap.put("url", url1);
        Placesref.child(Pname).updateChildren(placemap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminAddnewplaceActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();
            }
        });

    }

    // Select Image method
    private void openGallery() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                GalleryPick);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == GalleryPick
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            Imageuri = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                Imageuri);
                placeimage
                        .setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}
//public void updateplace(final String id){
//        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Places");
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child(id).exists()){
//                    HashMap<String,Object> update=new HashMap<>();
//                    update.put("Timings", Placetimings);
//                    update.put("EntryFee",Placefees);
//                    update.put("Img",downloadimageurl);
//                    update.put("url",url1);
//                    databaseReference.child(id).updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                Toast.makeText(AdminAddnewplaceActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//}
//
//}
