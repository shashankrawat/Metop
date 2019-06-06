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
import com.elintminds.mac.metatopos.activities.addnewpost.Adapers.AddNewServicesImagesAdapter;
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
import com.google.android.gms.location.places.Place;
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

public class ServicesDetailsFormActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private static final int STORAGE_PERMISSION_CODE = 786;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 200;
    private static final int CAMERA_REQUEST = 101;
    private static final int GALLARY_REQUEST = 102;
    public static final int requestCode_Location = 80;
    String imagePathNew, picturePath;
    Dialog dialog;
    TextInputEditText ed_Name, ed_Price, ed_Location, ed_Time, ed_Description;
    RadioGroup visibilty_Type;
    RadioButton visible_Everyone, visible_FollowersOnly;
    Button btn_Post;
    ImageView icon_back, upload_PostImages;
    RecyclerView post_ImagesContainer_recyclrview;
    LinearLayoutManager linearLayoutManager;
    AddNewServicesImagesAdapter addNewServicesImagesAdapter;
    ArrayList<Integer> data = new ArrayList();
    ProgressDialog m_Dialog;
    APIService mapiService;
    private String previousUploadFile = "";
    String city, state, country, zipcode, lat, lng, token;
    HashMap<String, Object> map = new HashMap<String, Object>();
    ArrayList<String> fileUploadData = new ArrayList<>();
    SharedPreferences preferences;
    String categoryID, subID, superID, filepath, SuperCategoryID, SubCategoryID;
    ArrayList<Attachments> attachmentsList = new ArrayList<>();
    String isFrom, editPostCategories;
    GetPostByIdData postdata;
    TextView tv_location_address;
    private GetLocation getLocation;
    List<Address> addresses = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_details_form);
        getLocation = new GetLocation(this);
        checkForPermissionLocation(this);
        initviews();
        mapiService = RetrofitClient.getClient().create(APIService.class);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        post_ImagesContainer_recyclrview.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        addNewServicesImagesAdapter = new AddNewServicesImagesAdapter(getApplicationContext(), fileUploadData);
        post_ImagesContainer_recyclrview.setAdapter(addNewServicesImagesAdapter);
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
            if (isFrom.equals("Services")) {
                ed_Name.setText(postdata.getTitle());
                ed_Price.setText(postdata.getPrice());
                ed_Description.setText(postdata.getDescription());
                ed_Location.setText(postdata.getAddress());
                ed_Time.setText(postdata.getTimeDuration());
                editPostCategories = postdata.getCategoryId();
                fileUploadData.add(postdata.getAttachments().get(0).getImageUrl());
                addNewServicesImagesAdapter = new AddNewServicesImagesAdapter(getApplicationContext(), fileUploadData);
                post_ImagesContainer_recyclrview.setAdapter(addNewServicesImagesAdapter);
                Attachments attachments = new Attachments();
                attachments.setAttachment(postdata.getAttachments().get(0).getImageUrl());
                attachmentsList.add(attachments);
                addNewServicesImagesAdapter.notifyDataSetChanged();
                city = postdata.getCity();
                state = postdata.getState();
                country = postdata.getCountry();
                zipcode = postdata.getZipcode();
                lat = postdata.getLatitude();
                lng = postdata.getLongitude();
            } else if (isFrom.equals("Category_Services")) {
                ed_Name.setText("");
                ed_Price.setText("");
                ed_Description.setText("");
                ed_Location.setText("");
                ed_Time.setText("");
            }
        }


    }

    private void initviews() {
        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);
        ed_Name = findViewById(R.id.service_name);
        ed_Price = findViewById(R.id.service_price);
        ed_Description = findViewById(R.id.service_Description);
        ed_Time = findViewById(R.id.service_timeDuration);
        ed_Location = findViewById(R.id.service_location);
        upload_PostImages = findViewById(R.id.upload_service_images);
        icon_back = findViewById(R.id.services_backbtn);
        post_ImagesContainer_recyclrview = findViewById(R.id.services_images_container_rv);
        visibilty_Type = findViewById(R.id.Service_visibility);
        visible_Everyone = findViewById(R.id.service_visible_everyone);
        visible_FollowersOnly = findViewById(R.id.service_visible_followers);
        btn_Post = findViewById(R.id.services_post_btn);
        tv_location_address = findViewById(R.id.tv_location_address);
        icon_back.setOnClickListener(this);
        ed_Location.setOnClickListener(this);
        upload_PostImages.setOnClickListener(this);
        ed_Time.setOnClickListener(this);
        btn_Post.setOnClickListener(this);
        ed_Time.setOnFocusChangeListener(this);
        ed_Location.setOnFocusChangeListener(this);
        tv_location_address.setOnClickListener(this);

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        if (view == icon_back) {
            finish();
        } else if (view == ed_Time) {
            popupmenu();

        } else if (view == tv_location_address) {
            ed_Location.setText(city);
        } else if (view == ed_Location) {
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

        } else if (view == upload_PostImages) {
            if (ContextCompat.checkSelfPermission(ServicesDetailsFormActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                showImagePickerDialog();
            } else {
                requestStoragePermission();
            }
        } else if (view == btn_Post) {
            if (ed_Name.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.enter_name, Toast.LENGTH_SHORT).show();
                ed_Name.requestFocus();

            } else if (ed_Price.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.enter_price, Toast.LENGTH_SHORT).show();
                ed_Price.requestFocus();
            } else if (ed_Description.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.enter_description, Toast.LENGTH_SHORT).show();
                ed_Description.requestFocus();
            } else if (ed_Time.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.select_time_duration, Toast.LENGTH_SHORT).show();
                ed_Time.requestFocus();
            } else if (ed_Location.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.enter_location, Toast.LENGTH_SHORT).show();
                ed_Location.requestFocus();
            } else if (addNewServicesImagesAdapter.getItemCount() == 0) {
                Toast.makeText(this, R.string.upload_atleast_one_image, Toast.LENGTH_SHORT).show();

            } else {
                if (isFrom.equals("Category_Services")) {
                    map.put("categoryId", SubCategoryID);
                    map.put("superCategoryId", SuperCategoryID);
                    map.put("subCategoryId", categoryID);
                    map.put("title", ed_Name.getText().toString());
                    map.put("description", ed_Description.getText().toString());
                    if (visible_Everyone.isChecked() == true) {
                        map.put("visibilityTypeId", "1");
                    } else if (visible_FollowersOnly.isChecked() == true) {
                        map.put("visibilityTypeId", "0");
                    }
                    map.put("address", ed_Location.getText().toString());
                    map.put("city", city);
                    map.put("state", state);
                    map.put("country", country);
                    map.put("zipcode", zipcode);
                    map.put("latitude", lat);
                    map.put("longitude", lng);
                    map.put("attachments", attachmentsList);
                    map.put("price", ed_Price.getText().toString().trim());
                    map.put("timeDuration", ed_Time.getText().toString());
                    saveNewPost(token, map);
                } else if (isFrom.equals("Services")) {
                    map.put("postid", postdata.getPostId());
                    map.put("categoryId", editPostCategories);
                    map.put("superCategoryId", postdata.getSuperCategoryId());
                    map.put("title", ed_Name.getText().toString());
                    map.put("description", ed_Description.getText().toString());
                    if (visible_Everyone.isChecked() == true) {
                        map.put("visibilityTypeId", "1");
                    } else if (visible_FollowersOnly.isChecked() == true) {
                        map.put("visibilityTypeId", "0");
                    }
                    map.put("address", ed_Location.getText().toString());
                    map.put("city", city);
                    map.put("state", state);
                    map.put("country", country);
                    map.put("zipcode", zipcode);
                    map.put("latitude", lat);
                    map.put("longitude", lng);
                    map.put("attachments", attachmentsList);
                    map.put("price", ed_Price.getText().toString().trim());
                    map.put("timeDuration", ed_Time.getText().toString());
                    saveNewPost(token, map);

                }

            }
        }


    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (ed_Time.hasFocus()) {
            popupmenu();
        } else if (ed_Location.hasFocus()) {
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


    void popupmenu() {

        PopupMenu popup = new PopupMenu(this, ed_Time);
        popup.getMenuInflater()
                .inflate(R.menu.time_hours_months_popup_menu_, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                ed_Time.setText(item.getTitle());
                return true;
            }
        });

        popup.show(); //showing popup menu


    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            ActivityCompat.requestPermissions(ServicesDetailsFormActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == STORAGE_PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
//                showImagePickerDialog();
//            } else {
//                Toast.makeText(this, "Permission DENIED ", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

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
                Place place = PlaceAutocomplete.getPlace(getApplicationContext(), data);
                Log.i("Location", "Place: " + place.getName());
                ed_Location.setText(place.getName());
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
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
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
                            Toast.makeText(ServicesDetailsFormActivity.this, R.string.image_uploaded, Toast.LENGTH_SHORT).show();
                            assert response.body() != null;
                            FileUploadData imgData = response.body().getfileData();
                            if (imgData != null) {
                                fileUploadData.add(imgData.getCustomPath());
                                filepath = imgData.getCustomPath();
                            }
                            Attachments attachments = new Attachments();
                            attachments.setAttachment(filepath);
                            attachmentsList.add(attachments);
                            addNewServicesImagesAdapter.notifyDataSetChanged();
                            m_Dialog.dismiss();
                        } else {
                            Toast.makeText(ServicesDetailsFormActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<FileupoladResponse> call, Throwable t) {
                    Log.e("FILE UPLOAD REG", "" + t.getMessage());
                    Toast.makeText(ServicesDetailsFormActivity.this, "Failed " + t.getMessage(), Toast.LENGTH_LONG).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveNewPost(String token, HashMap<String, Object> map) {
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
                            Toast.makeText(ServicesDetailsFormActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), R.string.you_have_denied_access_location_permission, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(getApplicationContext(), R.string.you_have_denied_access_location_permission, Toast.LENGTH_SHORT).show();
                }
            }

            case STORAGE_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
                    showImagePickerDialog();
                } else {
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
