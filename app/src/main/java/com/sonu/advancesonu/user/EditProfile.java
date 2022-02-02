package com.sonu.advancesonu.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


import com.sonu.advancesonu.R;
import com.sonu.advancesonu.customDialog.UpdatePhoneNumber;
import com.sonu.advancesonu.customDialog.UpdateUserName;
import com.sonu.advancesonu.main.Home;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.util.Random;


public class EditProfile extends AppCompatActivity implements View.OnClickListener , UpdateUserName.UpdateNameListener, UpdatePhoneNumber.UpdatePhoneNumberListener {
    private ImageButton edtName;
    private ImageButton edtNumber;
    private ImageButton edtProfileImage;
    private ImageView profileImage;
    private Uri profileImageUri;
    private FirebaseAuth mAuth;
    private TextView nameView;
    private TextView emailView;
    private TextView numberView;
//    private TextView addressView,pinView,cableOperatorView;
//    private ImageView edtAddress,edtPin,edtCableOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        inItUi();

    }
    public void inItUi()
    {
        edtName = findViewById(R.id.edt_name);
        edtNumber = findViewById(R.id.edt_number);
        profileImage = findViewById(R.id.profile_image);
        edtProfileImage = findViewById(R.id.edt_profile_image);
        mAuth = FirebaseAuth.getInstance();
        nameView = findViewById(R.id.name_view);
        emailView = findViewById(R.id.email_view);
        numberView = findViewById(R.id.number_view);

//        addressView = findViewById(R.id.address_view);
//        pinView = findViewById(R.id.pin_view);
//        cableOperatorView = findViewById(R.id.cable_operator_view);
//        edtAddress = findViewById(R.id.edt_address);
//        edtPin = findViewById(R.id.edt_pin);
//        edtCableOperator = findViewById(R.id.edt_cable_operator);

        edtName.setOnClickListener(this);
        edtNumber.setOnClickListener(this);
        edtProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .start(EditProfile.this);

            }
        });

        Picasso.get().load(Home.userInfo.getUserImage()).into(profileImage);
        nameView.setText(Home.userInfo.getUserName());
        emailView.setText(Home.userInfo.getUserEmail());
        numberView.setText(Home.userInfo.getUserMob());
//        addressView.setText(Home.userInfo.getAddress());
//        pinView.setText(Home.userInfo.getPin());
//        cableOperatorView.setText(Home.userInfo.getCableOperator());



    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.edt_profile_image: CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                    .start(EditProfile.this);
                break;
            case R.id.edt_name:
                UpdateUserName updateUserName = new UpdateUserName();
                updateUserName.setStyle(DialogFragment.STYLE_NO_FRAME,R.style.dialog);
                updateUserName.show(getSupportFragmentManager(),"updateName");
                break;
            case R.id.edt_number:UpdatePhoneNumber updateNumber = new UpdatePhoneNumber();
                                 updateNumber.setStyle(DialogFragment.STYLE_NO_FRAME,R.style.dialog);
                                 updateNumber.show(getSupportFragmentManager(),"updateNumber");
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
              //  Toast.makeText(EditProfile.this,"hello", Toast.LENGTH_SHORT).show();
                Uri resultUri = result.getUri();
                profileImage.setImageURI(resultUri);
                profileImageUri=resultUri;
                uploadImage(profileImageUri,generateImageName());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(EditProfile.this,result.getError()+"", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public void uploadImage(Uri file, String name) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        final StorageReference imageRef = storageRef.child("profile_image").child(name+".jpg");
        final UploadTask uploadTask = imageRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                Log.e("EditProfile","..........pro exe........."+exception.getMessage());
                Toast.makeText(EditProfile.this,"server error. please try after sometime.", Toast.LENGTH_SHORT).show();



            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //carInfo.put("image",uri.toString());
                        String imageUrl = uri.toString();
                        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).
                                child("image").setValue(imageUrl);
                        Toast.makeText(EditProfile.this,"image Updated", Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });
    }
    public String generateImageName() {
        Random rand = new Random();
        int rand_int1 = rand.nextInt(900) + 100;
        int rand_int2 = rand.nextInt(900) + 100;
        int rand_int3 = rand.nextInt(900) + 100;
        int rand_int4 = rand.nextInt(900) + 100;

        return  rand_int1 + rand_int2 + rand_int3+""+rand_int4;
    }


    @Override
    public void updateName(String name) {
        nameView.setText(name);
    }

    @Override
    public void updateNumber(String number) {
        numberView.setText(number);
    }
}
