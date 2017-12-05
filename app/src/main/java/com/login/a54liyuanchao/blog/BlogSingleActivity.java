package com.login.a54liyuanchao.blog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BlogSingleActivity extends AppCompatActivity {

    private String mPost_key = null;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private ImageView msingleblogimage;
    private TextView msingleblogtitle;
    private TextView msingleblogdesc;
    private Button msingleremoveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mAuth = FirebaseAuth.getInstance();

        msingleblogdesc = (TextView) findViewById(R.id.singledescField);
        msingleblogtitle = (TextView) findViewById(R.id.singletitleField);
        msingleblogimage = (ImageView) findViewById(R.id.singleImageSelect);
        msingleremoveBtn = (Button) findViewById(R.id.singleremoveBtn);

        mPost_key = getIntent().getExtras().getString("blog_id");
        //Toast.makeText(BlogSingleActivity.this, post_key, Toast.LENGTH_LONG).show();
        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_title = (String) dataSnapshot.child("Title").getValue();
                String post_desc = (String) dataSnapshot.child("Desc").getValue();
                String post_image = (String) dataSnapshot.child("Image").getValue();
                String post_uid = (String) dataSnapshot.child("Uid").getValue();

                msingleblogtitle.setText(post_title);
                msingleblogdesc.setText(post_desc);
                Picasso.with(BlogSingleActivity.this).load(post_image).into(msingleblogimage);

                if(mAuth.getCurrentUser().getUid().equals(post_uid)){

                    msingleremoveBtn.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        msingleremoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(mPost_key).removeValue();
                Intent mainintent = new Intent(BlogSingleActivity.this, MainActivity.class);
                startActivity(mainintent);

            }
        });
    }
}
