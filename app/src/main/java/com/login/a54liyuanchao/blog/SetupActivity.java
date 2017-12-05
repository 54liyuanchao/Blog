package com.login.a54liyuanchao.blog;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SetupActivity extends AppCompatActivity {

    private ImageButton mSetupImageBtn;
    private EditText mNameField;
    private Button mSubmitBtn;
    private ProgressBar mProgressBar;

    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;

    private StorageReference mStorageImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mSetupImageBtn = (ImageButton)findViewById(R.id.SetupImageBtn);
        mNameField = (EditText)findViewById(R.id.SetupNameField);
        mSubmitBtn = (Button)findViewById(R.id.SetupSubmitBtn);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar4);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_images");

        mAuth = FirebaseAuth.getInstance();

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startsetupaccount();

            }
        });

        mSetupImageBtn.setOnClickListener(new View.OnClickListener() {            // 如何打开相册
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });
    }

    private void startsetupaccount() {

        final String name = mNameField.getText().toString().trim();

        final String user_id = mAuth.getCurrentUser().getUid();
//先设置uri是null， 选中图片后URI赋值
        if(!TextUtils.isEmpty(name) && mImageUri != null){

            mProgressBar.setVisibility(View.VISIBLE);

            StorageReference filepath = mStorageImage.child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String downloadUri = taskSnapshot.getDownloadUrl().toString();

                    mDatabaseUsers.child(user_id).child("name").setValue(name);
                    mDatabaseUsers.child(user_id).child("image").setValue(downloadUri);

                    Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);

                    mProgressBar.setVisibility(View.INVISIBLE);

                }
            });



        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  //如何使用裁剪工具
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            Uri Imageuri = data.getData();

            CropImage.activity(Imageuri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();

                mSetupImageBtn.setImageURI(mImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }
}
