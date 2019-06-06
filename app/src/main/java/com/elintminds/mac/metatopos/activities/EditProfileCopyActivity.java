package com.elintminds.mac.metatopos.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.fileupload.FileupoladResponse;
import com.elintminds.mac.metatopos.beans.genEmoji.GenEmojiData;
import com.elintminds.mac.metatopos.beans.login.LoginData;
import com.elintminds.mac.metatopos.beans.login.ReportIssueResponse;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppConstants;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.io.File;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileCopyActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView edit_profilecopy_backbtn, iv_upload_new_profile_pic, gen_moji, iv_upload_genmoji;
    Dialog chnagepicdialog;
    CircleImageView user_Profile_pic;
    Button save_Profile;
    EditText user_full_name;
    APIService mapiService;
    ProgressDialog m_Dialog;
    private String previousUploadFile = "";
    private String picturePath, picturePathURL;
    SharedPreferences preferences;
    String token;
    String imagePathNew, previoursImagePath, previousEmoji;
    LoginData getProfiledata;
    String selectedImagePath, genmojiID;
    HashMap<String, String> map = new HashMap<String, String>();
    String emojipath, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_copy);
        edit_profilecopy_backbtn = findViewById(R.id.edit_profilecopy_backbtn);
        iv_upload_new_profile_pic = findViewById(R.id.iv_upload_new_profile_pic);
        iv_upload_genmoji = findViewById(R.id.upload_genmoji);
        user_Profile_pic = findViewById(R.id.user_dp);
        gen_moji = findViewById(R.id.edit_profile_genMoji);
        save_Profile = findViewById(R.id.save_profile_btn);
        user_full_name = findViewById(R.id.ed_fullname);
        edit_profilecopy_backbtn.setOnClickListener(this);
        iv_upload_new_profile_pic.setOnClickListener(this);
        iv_upload_genmoji.setOnClickListener(this);
        save_Profile.setOnClickListener(this);
        Intent i = getIntent();
        getProfiledata = (LoginData) i.getSerializableExtra("PROFILE_DATA");
        previoursImagePath = getProfiledata.getProfileImageUrl();
        previousEmoji = getProfiledata.getMojiImageUrl();
        Uri profilepicpath = Uri.parse("https://www.metatopos.elintminds.work/" + previoursImagePath);
        Uri emojipath = Uri.parse("https://www.metatopos.elintminds.work/" + previousEmoji);
        Glide.with(getApplicationContext()).load(profilepicpath).into(user_Profile_pic);
        Glide.with(getApplicationContext()).load(emojipath).into(gen_moji);
        user_full_name.setText(getProfiledata.getFullName());
        mapiService = RetrofitClient.getClient().create(APIService.class);
        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);
    }

    @Override
    public void onClick(View view) {
        if (view == edit_profilecopy_backbtn) {
            finish();
        } else if (view == iv_upload_new_profile_pic) {
            changeProfilepicDialog();
        } else if (view == iv_upload_genmoji) {
            Intent addmoji = new Intent(this, GenMojiActivity.class);
            startActivityForResult(addmoji, 101);
        } else if (view == save_Profile) {
            userName = user_full_name.getText().toString();
            map.put("profile_image_url", previoursImagePath);
            map.put("gen_moji_id", genmojiID);
            map.put("fullname", userName);
            updateUserProfile(token, map);
        }
    }

    private void changeProfilepicDialog() {
        Button remove_current_pic_Btn, camera_Picker_Btn, gallery_picker_Btn, cancel_Btn;
        chnagepicdialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        Window window = chnagepicdialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        chnagepicdialog.setTitle(null);
        chnagepicdialog.setContentView(R.layout.change_profile_pic_dialog);
        chnagepicdialog.setCancelable(true);
        chnagepicdialog.show();
        remove_current_pic_Btn = chnagepicdialog.findViewById(R.id.remove_current_photo_btn);
        camera_Picker_Btn = chnagepicdialog.findViewById(R.id.take_photo_btn);
        gallery_picker_Btn = chnagepicdialog.findViewById(R.id.choose_from_gallary_btn);
        cancel_Btn = chnagepicdialog.findViewById(R.id.cancel_Btn);

        remove_current_pic_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.put("previouspath", previoursImagePath);
                removeProfilePhoto(token, map);
            }
        });

        camera_Picker_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                    Uri fileUri = AppConstants.getOutputMediaFileUri(getApplicationContext());
                    if (fileUri != null) {
                        imagePathNew = fileUri.getPath();
                    }
                    Log.e("PATH", "" + imagePathNew);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(cameraIntent, 1);
                    chnagepicdialog.dismiss();
                }

            }
        });
        gallery_picker_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
                chnagepicdialog.dismiss();
            }
        });

        cancel_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chnagepicdialog.dismiss();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            Log.e("path image", "" + imagePathNew);
            Bitmap bm = null;
            if (imagePathNew != null) {
                bm = AppConstants.decodeBitmapFromSDCard(imagePathNew, 200, 200);
                int rotation = AppConstants.rotationAngle(imagePathNew);
                if (rotation != 0) {
                    Matrix mat = new Matrix();
                    mat.postRotate(rotation);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mat, true);
                    bm.recycle();
                    if (rotatedBitmap != null)
                        bm = rotatedBitmap;
                }

            }
            if (bm != null) {
                Uri tempUri = AppConstants.getImageUri(getApplicationContext(), bm);
                File finalFile = new File(AppConstants.getRealPathFromURI(getApplicationContext(), tempUri));
                selectedImagePath = finalFile.getAbsolutePath();
                Log.e("pathCapture", selectedImagePath);
                user_Profile_pic.setImageBitmap(bm);
                doFileUpoload(new File(selectedImagePath), "1");
            }


        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                Log.w("path of image from gallery......******************.........", picturePath+"");
            user_Profile_pic.setImageBitmap(thumbnail);
            File file = new File(picturePath);
            doFileUpoload(file, "1");
        } else if (requestCode == 101) {
            if (resultCode == RESULT_OK && data != null) {
                GenEmojiData genEmojiData = data.getParcelableExtra("EMOJI_DATA");
                genmojiID = genEmojiData.getId();
                emojipath = genEmojiData.getMojiImageUrl();
                Log.e("EmojiId", "" + genEmojiData.getId() + ", " + emojipath);
                Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + emojipath);
                Glide.with(EditProfileCopyActivity.this).load(path).into(gen_moji);
            }
        }
    }

    private void doFileUpoload(File imageFile, String fileTp) {
        m_Dialog = DialogUtils.showProgressDialog(this);
        final RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("userfile", imageFile.getName(), requestFile);
        RequestBody fileType = RequestBody.create(MediaType.parse("text/plain"), fileTp);
        RequestBody previousPath = RequestBody.create(MediaType.parse("text/plain"), previousUploadFile);
        m_Dialog.show();
        Call<FileupoladResponse> call = mapiService.FileUploadResponse(body, fileType, previousPath);
        call.enqueue(new Callback<FileupoladResponse>() {
            @Override
            public void onResponse(Call<FileupoladResponse> call, Response<FileupoladResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == true) {
                        Toast.makeText(EditProfileCopyActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                        iv_userProfilePic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                        previoursImagePath = response.body().getfileData().getCustomPath();
                        Log.e("PictureUrl", response.body().getfileData().getCustomPath());

                        Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + picturePathURL);
                        Glide.with(EditProfileCopyActivity.this).load(path).into(user_Profile_pic);
                        m_Dialog.dismiss();
                    } else {
                        Toast.makeText(EditProfileCopyActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        m_Dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<FileupoladResponse> call, Throwable t) {
                Log.e("FILE UPLOAD REG", "" + t.getMessage());
                Toast.makeText(EditProfileCopyActivity.this, "Failed " + t.getMessage(), Toast.LENGTH_LONG).show();
                m_Dialog.dismiss();
            }
        });
    }

    private void updateUserProfile(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            Call<ReportIssueResponse> call = mapiService.editProfile(token, map);
            final ProgressDialog m_Dialog = DialogUtils.showProgressDialog(this);
            m_Dialog.show();
            call.enqueue(new Callback<ReportIssueResponse>() {
                @Override
                public void onResponse(Call<ReportIssueResponse> call, Response<ReportIssueResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            Toast.makeText(EditProfileCopyActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            user_full_name.setText("");
                            finish();
                            m_Dialog.dismiss();
                        } else {
                            Toast.makeText(EditProfileCopyActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();


//                        if (response.body().getError().getProfileImageUrl() == null) {
//                            Toast.makeText(EditProfileCopyActivity.this, "" + response.body().getError().getProfileImageUrl(), Toast.LENGTH_SHORT).show();
//                        } else if (response.body().getError().getGenMojiId() == null) {
//                            Toast.makeText(EditProfileCopyActivity.this, "" + response.body().getError().getGenMojiId(), Toast.LENGTH_SHORT).show();
//                        } else if (response.body().getError().getFullname() == null) {
//                            Toast.makeText(EditProfileCopyActivity.this, "" + response.body().getError().getFullname(), Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(EditProfileCopyActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ReportIssueResponse> call, Throwable t) {

                    Toast.makeText(EditProfileCopyActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeProfilePhoto(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            Call<ReportIssueResponse> call = mapiService.removeProfilePhoto(token, map);
            final ProgressDialog m_Dialog = DialogUtils.showProgressDialog(this);
            m_Dialog.show();
            call.enqueue(new Callback<ReportIssueResponse>() {
                @Override
                public void onResponse(Call<ReportIssueResponse> call, Response<ReportIssueResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {

                            Toast.makeText(EditProfileCopyActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("Updated_Emoji_Path", emojipath);
                            intent.putExtra("Updated_Profile_Path", picturePath);
                            intent.putExtra("Updated_user_name", userName);
                            setResult(RESULT_OK, intent);
                            finish();
                            user_full_name.setText("");
                            m_Dialog.dismiss();
                        } else {
                            Toast.makeText(EditProfileCopyActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ReportIssueResponse> call, Throwable t) {

                    Toast.makeText(EditProfileCopyActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
