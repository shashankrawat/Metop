package com.elintminds.mac.metatopos.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.login.LoginData;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView edit_profile_backbtn, gen_moji;
    CircleImageView user_Profile_pic;
    EditText user_full_name;
    Bundle getbundle;
    Button edit_btn;
    LoginData getProfiledata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        edit_profile_backbtn = findViewById(R.id.edit_profile_backbtn);
        edit_btn = findViewById(R.id.edit_btn);
        user_Profile_pic = findViewById(R.id.user_dp);
        user_full_name = findViewById(R.id.ed_userfullname);
        gen_moji = findViewById(R.id.edit_profile_genMoji);
        edit_profile_backbtn.setOnClickListener(this);
        edit_btn.setOnClickListener(this);
        Intent i = getIntent();
        getProfiledata = (LoginData) i.getSerializableExtra("USER_PROFILE_DATA");
        String getprofile = getProfiledata.getProfileImageUrl();
        String getemoji = getProfiledata.getMojiImageUrl();
        Uri profilepicpath = Uri.parse("https://www.metatopos.elintminds.work/" + getprofile);
        Uri emojipath = Uri.parse("https://www.metatopos.elintminds.work/" + getemoji);
        Glide.with(getApplicationContext()).load(profilepicpath).into(user_Profile_pic);
        Glide.with(getApplicationContext()).load(emojipath).into(gen_moji);
        user_full_name.setText(getProfiledata.getFullName());
    }

    @Override
    public void onClick(View view) {
        if (view == edit_profile_backbtn) {
            finish();
        } else if (view == edit_btn) {
            Intent editprofile = new Intent(getApplicationContext(), EditProfileCopyActivity.class);
            editprofile.putExtra("PROFILE_DATA", getProfiledata);
            startActivityForResult(editprofile, 10);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK && data != null) {
                String emojipath = data.getStringExtra("Updated_Emoji_Path");
                String profilepicpath = data.getStringExtra("Updated_Profile_Path");
                String username = data.getStringExtra("Updated_user_name");
                Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + emojipath);
                Glide.with(EditProfileActivity.this).load(path).into(gen_moji);
                Uri path2 = Uri.parse("https://www.metatopos.elintminds.work/" + profilepicpath);
                Glide.with(EditProfileActivity.this).load(path2).into(user_Profile_pic);
                user_full_name.setText(username);


            }
        }
    }
}
