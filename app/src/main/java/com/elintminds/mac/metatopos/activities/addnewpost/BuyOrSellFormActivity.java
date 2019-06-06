package com.elintminds.mac.metatopos.activities.addnewpost;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.addnewpost.Adapers.AddNewPostImagesAdapter;
import com.elintminds.mac.metatopos.beans.fileupload.FileUploadData;
import com.elintminds.mac.metatopos.beans.fileupload.FileupoladResponse;
import com.elintminds.mac.metatopos.beans.getpost.GetPostByIdData;
import com.elintminds.mac.metatopos.beans.savepost.Attachments;
import com.elintminds.mac.metatopos.beans.savepost.SavePostResponse;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppConstants;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.GetLocation;
import com.elintminds.mac.metatopos.common.RetrofitClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyOrSellFormActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private static final int STORAGE_PERMISSION_CODE = 201;
    ImageView details_backbtn, iv_add_images;
    Dialog dialog;
    private static final int CAMERA_REQUEST = 101;
    private static final int GALLARY_REQUEST = 102;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 103;
    public static final int requestCode_Location = 80;
    String imagePathNew, picturePath;
    RecyclerView post_Images_recycerview;
    AddNewPostImagesAdapter postImagesAdapter;
    LinearLayoutManager linearLayoutManager;
    TextInputEditText ed_PostName, ed_PostPrice, ed_PostDescription, ed_PostCondition, ed_PostLocation;
    RadioButton rb_visibilityEveryone, rb_visibilityFollowers, rb_buy, rb_sell;
    TextView tv_location_address;
    RadioGroup rg_postVisibility, select_butOrSell;
    Button btn_Post;
    String imageEncoded;
    List<String> imagesEncodedList;
    ProgressDialog m_Dialog;
    APIService mapiService;
    private String previousUploadFile = "";
    String city, state, country, zipcode, lat, lng, token;
    HashMap<String, Object> map = new HashMap<String, Object>();
    List<Address> addresses = null;
    ArrayList<String> fileUploadData = new ArrayList<>();
    SharedPreferences preferences;
    String categoryID, filepath;
    ArrayList<Attachments> attachmentsList = new ArrayList<>();
    GetPostByIdData postdata;
    private GetLocation getLocation;
    String isFrom, editPostCategories, SuperCategoryID, editPostSuperCategories, SubCategoryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        mapiService = RetrofitClient.getClient().create(APIService.class);
        initviews();
        getLocation = new GetLocation(this);
        checkForPermissionLocation(this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        post_Images_recycerview.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        postImagesAdapter = new AddNewPostImagesAdapter(getApplicationContext(), fileUploadData);
        post_Images_recycerview.setAdapter(postImagesAdapter);
        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);
        Intent intent = getIntent();
        if (intent != null) {
            categoryID = intent.getStringExtra("ChildCategoryID");
            SuperCategoryID = intent.getStringExtra("SuperCategoryID");
            SubCategoryID = intent.getStringExtra("SubCategoryID");
            Log.e("CategoryID", "" + categoryID);
            Log.e("SuperID", "" + SuperCategoryID);
            Log.e("SuBID", "" + SubCategoryID);
            postdata = intent.getParcelableExtra("Edit_Post_Data");
            isFrom = intent.getStringExtra("isFromRemoveEditActivity");
        }
        if (isFrom.equals("Category_First")) {
            ed_PostName.setText("");
            ed_PostPrice.setText("");
            ed_PostCondition.setText("");
            ed_PostCondition.setText("");
            ed_PostDescription.setText("");
            ed_PostLocation.setText("");

        } else if (isFrom.equals("BuyOrSell")) {
            ed_PostName.setText(postdata.getTitle());
            ed_PostPrice.setText(postdata.getPrice());
            if (postdata.getConditionId().equals("0")) {
                ed_PostCondition.setText("Used");
            } else {
                ed_PostCondition.setText("New");
            }
            ed_PostDescription.setText(postdata.getDescription());
            ed_PostLocation.setText(postdata.getAddress());
            Log.e("ATTACHMENTS", "" + postdata.getAttachments());
            fileUploadData.add(postdata.getAttachments().get(0).getImageUrl());
            postImagesAdapter = new AddNewPostImagesAdapter(getApplicationContext(), fileUploadData);
            post_Images_recycerview.setAdapter(postImagesAdapter);
            Attachments attachments = new Attachments();
            attachments.setAttachment(postdata.getAttachments().get(0).getImageUrl());
            attachmentsList.add(attachments);
            postImagesAdapter.notifyDataSetChanged();
            editPostCategories = postdata.getCategoryId();
            editPostSuperCategories = postdata.getSuperCategoryId();
            city = postdata.getCity();
            state = postdata.getState();
            country = postdata.getCountry();
            zipcode = postdata.getZipcode();
            lat = postdata.getLatitude();
            lng = postdata.getLongitude();
            if (postdata.getVisibilityTypeId().equals("2")) {
                rb_visibilityFollowers.setChecked(true);
            } else if (postdata.getVisibilityTypeId().equals("1")) {
                rb_visibilityEveryone.setChecked(true);

            }
        }
    }

    private void initviews() {
        ed_PostName = findViewById(R.id.post_name);
        ed_PostPrice = findViewById(R.id.post_price);
        ed_PostDescription = findViewById(R.id.post_description);
        ed_PostCondition = findViewById(R.id.post_condition);
        ed_PostLocation = findViewById(R.id.post_location);
        tv_location_address = findViewById(R.id.tv_location_address);
        rb_visibilityEveryone = findViewById(R.id.rb_everyone);
        rb_visibilityFollowers = findViewById(R.id.rb_followers_only);
        rb_buy = findViewById(R.id.rb_buy);
        rb_sell = findViewById(R.id.rb_sell);
        rg_postVisibility = findViewById(R.id.visibility_group);
        select_butOrSell = findViewById(R.id.select_butOrSell);
        btn_Post = findViewById(R.id.post_btn);
        details_backbtn = findViewById(R.id.details_backbtn);
        iv_add_images = findViewById(R.id.iv_add_images);
        post_Images_recycerview = findViewById(R.id.post_images_container_Recyclerview);
        iv_add_images.setOnClickListener(this);
        details_backbtn.setOnClickListener(this);
        btn_Post.setOnClickListener(this);
        ed_PostLocation.setOnClickListener(this);
        ed_PostCondition.setOnClickListener(this);
        ed_PostCondition.setOnFocusChangeListener(this);
        ed_PostLocation.setOnFocusChangeListener(this);
        tv_location_address.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == details_backbtn) {
            finish();
        } else if (view == iv_add_images) {
            if (ContextCompat.checkSelfPermission(BuyOrSellFormActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                showImagePickerDialog();
            } else {
                requestStoragePermission();
            }
        } else if (view == btn_Post) {
            if (ed_PostName.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.enter_name, Toast.LENGTH_SHORT).show();
                ed_PostName.requestFocus();
            } else if (ed_PostPrice.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.enter_price, Toast.LENGTH_SHORT).show();
                ed_PostPrice.requestFocus();
            } else if (ed_PostDescription.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.enter_description, Toast.LENGTH_SHORT).show();
                ed_PostDescription.requestFocus();
            } else if (ed_PostCondition.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.select_condition, Toast.LENGTH_SHORT).show();
                ed_PostCondition.requestFocus();
            } else if (ed_PostLocation.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.enter_location, Toast.LENGTH_SHORT).show();
                ed_PostLocation.requestFocus();
            } else if (postImagesAdapter.getItemCount() == 0) {
                Toast.makeText(this, R.string.upload_atleast_one_image, Toast.LENGTH_SHORT).show();

            } else {
                if (isFrom.equals("Category_First")) {
                    map.put("categoryId", categoryID);
                    map.put("superCategoryId", SuperCategoryID);
                    map.put("subCategoryId", SubCategoryID);
                    if (rb_buy.isChecked() == true) {
                        map.put("buyorsell", "1");
                    } else if (rb_sell.isChecked() == true) {
                        map.put("buyorsell", "2");
                    }
                    map.put("title", ed_PostName.getText().toString());
                    map.put("description", ed_PostDescription.getText().toString());
                    if (rb_visibilityEveryone.isChecked() == true) {
                        map.put("visibilityTypeId", "1");
                    } else if (rb_visibilityFollowers.isChecked() == true) {
                        map.put("visibilityTypeId", "2");
                    }
                    map.put("address", ed_PostLocation.getText().toString());
                    map.put("city", city);
                    map.put("state", state);
                    map.put("country", country);
                    map.put("zipcode", zipcode);
                    map.put("latitude", lat);
                    map.put("longitude", lng);
                    map.put("attachments", attachmentsList);
                    map.put("price", ed_PostPrice.getText().toString().trim());
                    if (ed_PostCondition.getText().toString().equalsIgnoreCase("New")) {
                        map.put("conditionId", "1");
                    } else if (ed_PostCondition.getText().toString().equalsIgnoreCase("Used")) {
                        map.put("conditionId", "0");
                    }
                    saveNewPost(token, map);
                } else if (isFrom.equals("BuyOrSell")) {
                    map.put("postid", postdata.getPostId());
                    map.put("categoryId", editPostCategories);
                    map.put("superCategoryId", editPostSuperCategories);
                    map.put("title", ed_PostName.getText().toString());
                    map.put("description", ed_PostDescription.getText().toString());
                    if (rb_visibilityEveryone.isChecked() == true) {
                        map.put("visibilityTypeId", "1");
                    } else if (rb_visibilityFollowers.isChecked() == true) {
                        {
                            map.put("visibilityTypeId", "2");
                        }
                    }
                    map.put("address", ed_PostLocation.getText().toString());
                    map.put("city", city);
                    map.put("state", state);
                    map.put("country", country);
                    map.put("zipcode", zipcode);
                    map.put("latitude", lat);
                    map.put("longitude", lng);
                    map.put("attachments", attachmentsList);
                    map.put("price", ed_PostPrice.getText().toString().trim());
                    if (ed_PostCondition.getText().toString().equalsIgnoreCase("New")) {
                        map.put("conditionId", "1");
                    } else if (ed_PostCondition.getText().toString().equalsIgnoreCase("Used")) {
                        map.put("conditionId", "0");
                    }
                    saveNewPost(token, map);
                }
            }
        } else if (view == ed_PostLocation) {
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                // TODO: Handle the error.
            } catch (GooglePlayServicesNotAvailableException e) {
                // TODO: Handle the error.
            }
        } else if (view == ed_PostCondition) {
            popupmenu();
        } else if (view == tv_location_address) {

            ed_PostLocation.setText(city);
        }

    }

    @Override
    public void onFocusChange(View focusview, boolean hasFocus) {
        if (focusview == ed_PostCondition) {
            if (hasFocus) {
                popupmenu();
            }

        } else if (focusview == ed_PostLocation) {
            if (hasFocus) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }

        }

    }

    void popupmenu() {

        PopupMenu popup = new PopupMenu(this, ed_PostCondition);
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                ed_PostCondition.setText(item.getTitle());
                return true;
            }
        });

        popup.show(); //showing popup menu


    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            ActivityCompat.requestPermissions(BuyOrSellFormActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
            File file = new File(picturePath);
            doFileUpoload(file, "1");
        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlaceAutocomplete.getPlace(getApplicationContext(), data);
                Log.i("Location", "Place: " + place.getName());

                ed_PostLocation.setText(place.getAddress());
                Geocoder gcd = new Geocoder(this, Locale.getDefault());

                try {
                    addresses = gcd.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size() > 0) {
                    Log.e("country", addresses.get(0).getCountryName() + " City " + addresses.get(0).getLocality() + " zipcode" + addresses.get(0).getPostalCode() + "state" + addresses.get(0).getAdminArea());
                    country = addresses.get(0).getCountryName();
                    city = addresses.get(0).getLocality();
                    zipcode = addresses.get(0).getPostalCode();
                    state = addresses.get(0).getAdminArea();
                    lat = String.valueOf(addresses.get(0).getLatitude());
                    lng = String.valueOf(addresses.get(0).getLongitude());


                }


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);

                // TODO: Handle the error.
                Log.i("Location", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    private void doFileUpoload(File imageFile, final String fileTp) {
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
                            Toast.makeText(BuyOrSellFormActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                            assert response.body() != null;

                            FileUploadData imgData = response.body().getfileData();
                            if (imgData != null) {
                                fileUploadData.add(imgData.getCustomPath());
                                filepath = imgData.getCustomPath();
                            }
                            Attachments attachments = new Attachments();
                            attachments.setAttachment(filepath);
                            attachmentsList.add(attachments);
                            postImagesAdapter.notifyDataSetChanged();
                            Log.e("POST ATTACHMENT", "" + filepath);
                            m_Dialog.dismiss();
                        } else {
                            Toast.makeText(BuyOrSellFormActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<FileupoladResponse> call, Throwable t) {
                    Log.e("FILE UPLOAD REG", "" + t.getMessage());
                    Toast.makeText(BuyOrSellFormActivity.this, "Failed " + t.getMessage(), Toast.LENGTH_LONG).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveNewPost(String token, HashMap<String, Object> map) {
        Log.e("Save Post Data", "" + map);
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            Log.e("Follow", "" + map);
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<SavePostResponse> call = mapiService.savePost(token, map);
            m_Dialog = DialogUtils.showProgressDialog(this);
            m_Dialog.show();
            call.enqueue(new Callback<SavePostResponse>() {
                @Override
                public void onResponse(Call<SavePostResponse> call, Response<SavePostResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            Toast.makeText(BuyOrSellFormActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SavePostResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void checkForPermissionLocation(Context context) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                alertdialogMesg();
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode_Location);
            }
        } else {
            if (getLocation.enabledLocation()) {
                getLocation.getUserLocation();
                lat = String.valueOf(getLocation.getUserLatitude());
                lng = String.valueOf(getLocation.getUserLongitude());
                Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    addresses = gcd.getFromLocation(getLocation.getUserLatitude(), getLocation.getUserLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size() > 0) {
                    Log.e("country", addresses.get(0).getCountryName() + " City " + addresses.get(0).getLocality() + " zipcode" + addresses.get(0).getPostalCode() + "state" + addresses.get(0).getAdminArea());
                    country = addresses.get(0).getCountryName();
                    city = addresses.get(0).getLocality();
                    zipcode = addresses.get(0).getPostalCode();
                    state = addresses.get(0).getAdminArea();
                    lat = String.valueOf(addresses.get(0).getLatitude());
                    lng = String.valueOf(addresses.get(0).getLongitude());
                }

            } else {
                Toast.makeText(getApplicationContext(), "You have denied access location request", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void alertdialogMesg() {
        AlertDialog.Builder mybulider = new AlertDialog.Builder(getApplicationContext());
        mybulider.setTitle("Alert Message");
        mybulider.setMessage(R.string.Alert_Explanation_Location);
        mybulider.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode_Location);
            }
        });
        AlertDialog ad = mybulider.create();
        ad.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case requestCode_Location: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (getLocation.enabledLocation()) {
                        getLocation.getUserLocation();
                        lat = String.valueOf(getLocation.getUserLatitude());
                        lng = String.valueOf(getLocation.getUserLongitude());
                        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());

                        try {
                            addresses = gcd.getFromLocation(getLocation.getUserLatitude(), getLocation.getUserLongitude(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (addresses.size() > 0) {
                            Log.e("country", addresses.get(0).getCountryName() + " City " + addresses.get(0).getLocality() + " zipcode" + addresses.get(0).getPostalCode() + "state" + addresses.get(0).getAdminArea());
                            country = addresses.get(0).getCountryName();
                            city = addresses.get(0).getLocality();
                            zipcode = addresses.get(0).getPostalCode();
                            state = addresses.get(0).getAdminArea();
                            lat = String.valueOf(addresses.get(0).getLatitude());
                            lng = String.valueOf(addresses.get(0).getLongitude());
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You have denied access location request", Toast.LENGTH_SHORT).show();
                }
            }

            case STORAGE_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
                    showImagePickerDialog();
                } else {
                    Toast.makeText(this, "Permission DENIED ", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
