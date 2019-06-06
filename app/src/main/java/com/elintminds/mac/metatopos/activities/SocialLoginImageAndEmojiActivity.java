
package com.elintminds.mac.metatopos.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.fileupload.FileupoladResponse;
import com.elintminds.mac.metatopos.beans.genEmoji.GenEmojiData;
import com.elintminds.mac.metatopos.beans.register.UpdateLanguageResponse;
import com.elintminds.mac.metatopos.beans.selectlanguage.SelectLanguageData;
import com.elintminds.mac.metatopos.beans.selectlanguage.SelectLanguageResponse;
import com.elintminds.mac.metatopos.beans.updatesocialloginuserprofile.UpdateSocialLoginUserProfileResponse;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppConstants;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SocialLoginImageAndEmojiActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int STORAGE_PERMISSION_CODE = 201;
    ImageView iv_add_moji, iv_GenMoji, iv_upload_pic;
    CircleImageView iv_userProfilePic;
    Button btn_Register;
    TextView tv_login;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String[] PERMISSIONS = {Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    String[] CAMERA_PERMISSION = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    Snackbar snackbar;
    View sbView, view;
    LayoutInflater mInflater, layoutInflater;
    LinearLayout languages_btn_container;
    Dialog dialog;
    APIService mapiService;
    private static final int CAMERA_REQUEST = 101;
    private static final int GALLARY_REQUEST = 102;
    private static final int GOTOGENMOJI_REQUEST = 103;
    File file, destination;
    ProgressDialog m_Dialog;
    private String previousUploadFile = "", user_ID, language_ID, imagePathNew, picturePath, picturePathURL;
    GenEmojiData genEmojiData;
    int selectionPosition = -1, i, childcount, previousTag = -1;
    List<SelectLanguageData> language_list;
    HashMap<String, String> map = new HashMap<String, String>();
    String token;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login_image_and_emoji);


        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);
        user_ID = preferences.getString("LOGGED_UDERID", null);
        iv_add_moji = findViewById(R.id.iv_add_moji);
        iv_userProfilePic = findViewById(R.id.iv_userProfilePic);
        iv_GenMoji = findViewById(R.id.iv_gen_Moji);
        iv_upload_pic = findViewById(R.id.iv_uploadPic);
        tv_login = findViewById(R.id.tv_login);
        btn_Register = findViewById(R.id.btn_register);
        iv_add_moji.setOnClickListener(this);
        iv_upload_pic.setOnClickListener(this);
        btn_Register.setOnClickListener(this);
        mapiService = RetrofitClient.getClient().create(APIService.class);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if (view == iv_add_moji) {
            Intent addmoji = new Intent(this, GenMojiActivity.class);
            startActivityForResult(addmoji, GOTOGENMOJI_REQUEST);
        } else if (view == iv_upload_pic) {

            if (ContextCompat.checkSelfPermission(SocialLoginImageAndEmojiActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                showImagePickerDialog();
            } else {
                requestStoragePermission();
            }

        } else if (view == btn_Register) {
            map.put("profile_image_url", picturePathURL);
            map.put("gen_moji_id", genEmojiData.getId());
            UpdateProfile(token, map);

        }

    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            ActivityCompat.requestPermissions(SocialLoginImageAndEmojiActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
                showImagePickerDialog();
            } else {
                Toast.makeText(this, "Permission DENIED ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showImagePickerDialog() {
        Button camera_Picker_Btn, gallery_picker_Btn, cancel_Btn;
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        dialog.setTitle(null);
        dialog.setContentView(R.layout.choose_image_picker_layout);
        dialog.setCancelable(true);
        dialog.show();

        camera_Picker_Btn = dialog.findViewById(R.id.camera_picker_btn);
        gallery_picker_Btn = dialog.findViewById(R.id.gallery_picker_btn);
        cancel_Btn = dialog.findViewById(R.id.cancelBtn);

        camera_Picker_Btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                    Uri fileUri = AppConstants.getOutputMediaFileUri(getApplicationContext());
                    if (fileUri != null) {
                        imagePathNew = fileUri.getPath();
                    }
                    Log.e("PATH", "" + imagePathNew);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    dialog.dismiss();
                }

            }
        });


        gallery_picker_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLARY_REQUEST);
                dialog.dismiss();
            }
        });

        cancel_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }


    public boolean checkAppPermission(String[] reqPermissions) {
        for (String permission : reqPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPreviouslyDenied(String[] reqPermissions) {
        for (String permission : reqPermissions) {
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
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
                String selectedImagePath = finalFile.getAbsolutePath();
                Log.e("pathCapture", selectedImagePath);
//                    user_Profile_pic.setImageBitmap(bm);
                doFileUpoload(new File(selectedImagePath), "1");
            }

        } else if (requestCode == GALLARY_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            file = new File(picturePath);
            doFileUpoload(file, "1");
        } else if (requestCode == GOTOGENMOJI_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                genEmojiData = data.getParcelableExtra("EMOJI_DATA");
                Log.e("EmojiId", "" + genEmojiData.getId() + ", " + genEmojiData.getMojiImageUrl());
                Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + genEmojiData.getMojiImageUrl());
                Glide.with(SocialLoginImageAndEmojiActivity.this).load(path).into(iv_GenMoji);
            }
        }
    }

    private void doFileUpoload(File imageFile, String fileTp) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            m_Dialog = DialogUtils.showProgressDialog(this);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
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
                            picturePathURL = response.body().getfileData().getCustomPath();
                            Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + picturePathURL);
                            Glide.with(SocialLoginImageAndEmojiActivity.this).load(path).into(iv_userProfilePic);
                            m_Dialog.dismiss();
                        } else {
                            Toast.makeText(SocialLoginImageAndEmojiActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<FileupoladResponse> call, Throwable t) {
                    Log.e("FILE UPLOAD REG", "" + t.getMessage());
                    Toast.makeText(SocialLoginImageAndEmojiActivity.this, "Failed " + t.getMessage(), Toast.LENGTH_LONG).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void UpdateProfile(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            m_Dialog = DialogUtils.showProgressDialog(this);
            m_Dialog.show();
            Call<UpdateSocialLoginUserProfileResponse> call = mapiService.updateUserPhoto(token, map);
            call.enqueue(new Callback<UpdateSocialLoginUserProfileResponse>() {
                @Override
                public void onResponse(@NonNull Call<UpdateSocialLoginUserProfileResponse> call, @NonNull Response<UpdateSocialLoginUserProfileResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            Toast.makeText(SocialLoginImageAndEmojiActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
//                            user_ID = response.body().getData().getUserID();
                            showselectlangDialog();
                        } else {

                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UpdateSocialLoginUserProfileResponse> call, Throwable t) {
                    Toast.makeText(SocialLoginImageAndEmojiActivity.this, "" + t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();

                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void showselectlangDialog() {
        ImageView closebtn;
        Button done_btn;
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.select_language_popup);
        dialog.show();
        closebtn = dialog.findViewById(R.id.close_btn);
        done_btn = dialog.findViewById(R.id.btn_Done);
        languages_btn_container = dialog.findViewById(R.id.languages_btn_container);
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
//        TextView textView=view.findViewById(R.id.tv_show_language);


        getLanguages();
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                map.put("languageId", language_ID);
                map.put("userId", user_ID);
                updateLanguage(map);

            }
        });
    }

    private void getLanguages() {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            Call<SelectLanguageResponse> call = mapiService.selectLanguageResponse();
            call.enqueue(new Callback<SelectLanguageResponse>() {
                @Override
                public void onResponse(Call<SelectLanguageResponse> call, Response<SelectLanguageResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            Log.e("Language Data", "" + response.body().getData().getLanguages());
                            language_list = response.body().getData().getLanguages();
                            for (i = 0; i < language_list.size(); i++) {
                                // Add the text layout to the parent layout
                                view = layoutInflater.inflate(R.layout.language_item_view, languages_btn_container, false);
                                boolean islanguageSelected = false;
                                final TextView tv_language = view.findViewById(R.id.tv_show_language);
                                tv_language.setText(language_list.get(i).getDESCRIPTION());
                                Log.e("hghjssf", "" + language_list);
                                languages_btn_container.addView(tv_language);
                            }
                            languageSelectClickListener();
                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getError(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<SelectLanguageResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();


                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void languageSelectClickListener() {
        int count = languages_btn_container.getChildCount();
        if (count > 0) {
            for (childcount = 0; childcount < count; childcount++) {
                final View childView = languages_btn_container.getChildAt(childcount);
                childView.setTag(childcount);
                childView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (previousTag != -1) {
                            languages_btn_container.getChildAt(previousTag).setSelected(false);
                        }
                        previousTag = (int) view.getTag();
                        childView.setSelected(true);
                        Log.e("LanguageID", "" + language_list.get(previousTag).getSUBCODE());
                        language_ID = language_list.get(previousTag).getSUBCODE();

                    }

                });
            }
        }
    }

    private void updateLanguage(HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<UpdateLanguageResponse> call = mapiService.updateLanguage(map);
            m_Dialog = DialogUtils.showProgressDialog(this);
            m_Dialog.show();
            call.enqueue(new Callback<UpdateLanguageResponse>() {
                @Override
                public void onResponse(Call<UpdateLanguageResponse> call, Response<UpdateLanguageResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            Log.e("Update_Language", "" + response.body().getMessage());
                            m_Dialog.dismiss();
                            Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(home);
                            finish();

                        } else {
                            Log.e("UPdate_Language_Error", "" + response.body().getMessage());
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UpdateLanguageResponse> call, Throwable t) {
                    Log.e("OnFailed", "" + t.getMessage());
                    Toast.makeText(SocialLoginImageAndEmojiActivity.this, "Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
            dialog.dismiss();
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
