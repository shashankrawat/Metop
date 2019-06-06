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
import android.graphics.Point;
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
import android.support.v7.widget.AppCompatSeekBar;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.addnewpost.Adapers.AddNewAdvertisementImagesAdapter;
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
import java.util.StringTokenizer;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvertisementDetailsFormActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private static final int STORAGE_PERMISSION_CODE = 201;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 200;
    private static final int CAMERA_REQUEST = 101;
    private static final int GALLARY_REQUEST = 102;
    public static final int requestCode_Location = 80;
    String imagePathNew, picturePath;
    Dialog dialog;
    TextInputEditText advertisementName, advertisementDescription, advertisementLocation, advertisementTime, advertisementLink;
    TextView showseekbarrange;
    AppCompatSeekBar rangeOnMapSeekbar;
    ImageView icon_back, upload_Advertisement_pics;
    RadioGroup advertisementVisibility;
    RadioButton rb_everyone, rb_followersOnly;
    Button btnPostAdvertisement;
    RecyclerView advertiment_image_container_recyclrView;
    LinearLayoutManager linearLayoutManager;
    AddNewAdvertisementImagesAdapter addNewAdvertisementImagesAdapter;
    ProgressDialog m_Dialog;
    APIService mapiService;
    private String previousUploadFile = "";
    String city, state, country, zipcode, lat, lng, token;
    HashMap<String, Object> map = new HashMap<String, Object>();
    ArrayList<String> fileUploadData = new ArrayList<>();
    SharedPreferences preferences;
    String categoryID, filepath;
    int maxX;
    ArrayList<Attachments> attachmentsList = new ArrayList<>();
    String isFrom, editPostCategories;
    GetPostByIdData postdata;
    TextView tv_location_address;
    private GetLocation getLocation;
    List<Address> addresses = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement_details_form);
        getLocation = new GetLocation(this);
        checkForPermissionLocation(this);
        mapiService = RetrofitClient.getClient().create(APIService.class);
        initviews();
        rangeOnMapSeekbar.setProgress(50);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        advertiment_image_container_recyclrView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        addNewAdvertisementImagesAdapter = new AddNewAdvertisementImagesAdapter(getApplicationContext(), fileUploadData);
        advertiment_image_container_recyclrView.setAdapter(addNewAdvertisementImagesAdapter);
        Intent intent = getIntent();
        if (intent != null) {
            categoryID = intent.getStringExtra("SuperCATEGORYID");
            Log.e("SuperCATEGORYID", "" + categoryID);
            postdata = intent.getParcelableExtra("Edit_Post_Data");
            isFrom = intent.getStringExtra("isFromRemoveEditActivity");
        }
        if (isFrom.equals("Category_Advertisement")) {
            advertisementName.setText("");
            advertisementDescription.setText("");
            advertisementTime.setText("");
            advertisementLocation.setText("");
            advertisementLink.setText("");
        } else if (isFrom.equals("Advertisements")) {
            advertisementName.setText(postdata.getTitle());
            advertisementDescription.setText(postdata.getDescription());
            advertisementLink.setText(postdata.getExternalLink());
            advertisementLocation.setText(postdata.getAddress());
            advertisementTime.setText(postdata.getTimeDuration());
            rangeOnMapSeekbar.setProgress(Integer.parseInt(postdata.getMaxRangeOnMap()));
            showseekbarrange.setText(postdata.getMaxRangeOnMap() + "km");
            fileUploadData.add(postdata.getAttachments().get(0).getImageUrl());
            addNewAdvertisementImagesAdapter = new AddNewAdvertisementImagesAdapter(getApplicationContext(), fileUploadData);
            advertiment_image_container_recyclrView.setAdapter(addNewAdvertisementImagesAdapter);
            Attachments attachments = new Attachments();
            attachments.setAttachment(postdata.getAttachments().get(0).getImageUrl());
            attachmentsList.add(attachments);
            addNewAdvertisementImagesAdapter.notifyDataSetChanged();
            advertisementTime.setText(postdata.getTimeDuration());
//            if (postdata.getTimeDuration().equals("1")) {
//                 + " Hr");
//            } else {
//                advertisementTime.setText(postdata.getTimeDuration() + " Hrs");
//            }
            editPostCategories = postdata.getSuperCategoryId();
            city = postdata.getCity();
            state = postdata.getState();
            country = postdata.getCountry();
            zipcode = postdata.getZipcode();
            lat = postdata.getLatitude();
            lng = postdata.getLongitude();
            if (postdata.getVisibilityTypeId().equals("0")) {
                rb_followersOnly.setChecked(true);
            } else if (postdata.getVisibilityTypeId().equals("1")) {
                rb_everyone.setChecked(true);

            }
        }

        rangeOnMapSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showseekbarrange.setText("" + rangeOnMapSeekbar.getProgress());
                Point maxSizePoint = new Point();
                getWindowManager().getDefaultDisplay().getSize(maxSizePoint);
                maxX = maxSizePoint.x;
                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                showseekbarrange.setText("" + progress + "km");
                int textViewX = val - (showseekbarrange.getWidth() / 2);
                int finalX = showseekbarrange.getWidth() + textViewX > maxX ? (maxX - showseekbarrange.getWidth() - 16) : textViewX + 16/*your margin*/;
                showseekbarrange.setX(finalX < 0 ? 16/*your margin*/ : finalX);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    private void initviews() {
        icon_back = findViewById(R.id.advertisement_details_backbtn);
        showseekbarrange = findViewById(R.id.showseekbarrange);
        advertisementName = findViewById(R.id.advertisement_name);
        advertisementDescription = findViewById(R.id.advertisement_Description);
        advertisementTime = findViewById(R.id.advertisement_time_duration);
        advertisementLocation = findViewById(R.id.advertisement_location);
        advertisementLink = findViewById(R.id.advertisement_link);
        rangeOnMapSeekbar = findViewById(R.id.rangeonmap_seekbar);
        upload_Advertisement_pics = findViewById(R.id.upload_advertisement_images);
        advertisementVisibility = findViewById(R.id.advertisement_visibility_group);
        rb_everyone = findViewById(R.id.advertisement_visibility_everyone);
        rb_followersOnly = findViewById(R.id.advertisement_visibility_followers);
        btnPostAdvertisement = findViewById(R.id.advertisement_post_button);
        advertiment_image_container_recyclrView = findViewById(R.id.advertisemnt_images_container_rv);
        tv_location_address = findViewById(R.id.tv_location_address);
        icon_back.setOnClickListener(this);
        advertisementLocation.setOnClickListener(this);
        upload_Advertisement_pics.setOnClickListener(this);
        advertisementTime.setOnClickListener(this);
        btnPostAdvertisement.setOnClickListener(this);
        advertisementLocation.setOnFocusChangeListener(this);
        advertisementTime.setOnFocusChangeListener(this);
        tv_location_address.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        if (view == icon_back) {
            finish();
        } else if (view == tv_location_address) {
            advertisementLocation.setText(city);
        } else if (view == advertisementLocation) {
            try {
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                // TODO: Handle the error.
            } catch (GooglePlayServicesNotAvailableException e) {
                // TODO: Handle the error.
            }
        } else if (view == upload_Advertisement_pics) {
            if (ContextCompat.checkSelfPermission(AdvertisementDetailsFormActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                showImagePickerDialog();
            } else {
                requestStoragePermission();
            }
        } else if (view == advertisementTime) {
            popupmenu();
//            Calendar mcurrentTime = Calendar.getInstance();
//            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//            int minute = mcurrentTime.get(Calendar.MINUTE);
//            TimePickerDialog mTimePicker;
//            mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                    advertisementTime.setText(selectedHour + ":" + selectedMinute);
//                }
//            }, hour, minute, true);//Yes 24 hour time
//            mTimePicker.setTitle("Select Time");
//            mTimePicker.show();
        } else if (view == btnPostAdvertisement) {
            if (advertisementName.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please enter Name", Toast.LENGTH_SHORT).show();
                advertisementName.requestFocus();
            } else if (showseekbarrange.getText().toString().equals("")) {
                Toast.makeText(this, "Please select Range on Map ", Toast.LENGTH_SHORT).show();
                rangeOnMapSeekbar.requestFocus();
            } else if (advertisementDescription.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please enter Description", Toast.LENGTH_SHORT).show();
                advertisementDescription.requestFocus();
            } else if (advertisementTime.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please select Time Duration", Toast.LENGTH_SHORT).show();
                advertisementTime.requestFocus();
            } else if (advertisementLocation.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please enter Location", Toast.LENGTH_SHORT).show();
                advertisementLocation.requestFocus();
            } else if (advertisementLink.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please enter Link", Toast.LENGTH_SHORT).show();
                advertisementLink.requestFocus();
            } else if (addNewAdvertisementImagesAdapter.getItemCount() == 0) {
                Toast.makeText(this, "Please upload atleast one Image", Toast.LENGTH_SHORT).show();
            } else {
                preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
                token = preferences.getString("User_Token", null);
                if (isFrom.equals("Category_Advertisement")) {
                    map.put("superCategoryId", categoryID);
                    map.put("title", advertisementName.getText().toString());
                    map.put("description", advertisementDescription.getText().toString());
                    if (rb_everyone.isChecked() == true) {
                        map.put("visibilityTypeId", "1");
                    } else if (rb_followersOnly.isChecked() == true) {
                        map.put("visibilityTypeId", "0");
                    }
                    map.put("address", advertisementLocation.getText().toString());
                    map.put("city", city);
                    map.put("state", state);
                    map.put("country", country);
                    map.put("zipcode", zipcode);
                    map.put("latitude", lat);
                    map.put("longitude", lng);
                    map.put("attachments", attachmentsList);
                    String timeduration = advertisementTime.getText().toString();
                    StringTokenizer tokens2 = new StringTokenizer(timeduration, " ");
                    String time = tokens2.nextToken();
                    String string_Hours = tokens2.nextToken();
                    map.put("timeDuration", timeduration);
                    map.put("maxRangeOnMap", showseekbarrange.getText().toString());
                    map.put("externalLink", advertisementLink.getText().toString());
                    saveNewPost(token, map);
                } else if (isFrom.equals("Advertisements")) {
                    map.put("postid", postdata.getPostId());
                    map.put("superCategoryId", editPostCategories);
                    map.put("title", advertisementName.getText().toString());
                    map.put("description", advertisementDescription.getText().toString());
                    if (rb_everyone.isChecked() == true) {
                        map.put("visibilityTypeId", "1");
                    } else if (rb_followersOnly.isChecked() == true) {
                        map.put("visibilityTypeId", "0");
                    }
                    map.put("address", advertisementLocation.getText().toString());
                    map.put("city", city);
                    map.put("state", state);
                    map.put("country", country);
                    map.put("zipcode", zipcode);
                    map.put("latitude", lat);
                    map.put("longitude", lng);
                    map.put("attachments", attachmentsList);
                    String timeduration = advertisementTime.getText().toString();
                    StringTokenizer tokens2 = new StringTokenizer(timeduration, " ");
                    String time = tokens2.nextToken();
                    map.put("timeDuration", time);
                    map.put("maxRangeOnMap", showseekbarrange.getText().toString());
                    map.put("externalLink", advertisementLink.getText().toString());
                    saveNewPost(token, map);
                }

            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onFocusChange(View focusview, boolean hasFocus) {
        if (focusview == advertisementTime) {
            if (hasFocus) {
                popupmenu();
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        advertisementTime.setText(selectedHour + ":" + selectedMinute);
//                    }
//                }, hour, minute, true);//Yes 24 hour time
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
            }

        } else if (focusview == advertisementLocation) {
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

        PopupMenu popup = new PopupMenu(this, advertisementTime);
        popup.getMenuInflater()
                .inflate(R.menu.timeduration_popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                advertisementTime.setText(item.getTitle());
                return true;
            }
        });
        popup.show();
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            ActivityCompat.requestPermissions(AdvertisementDetailsFormActivity.this,
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
            File file = new File(picturePath);
            doFileUpoload(file, "1");
        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getApplicationContext(), data);
                Log.i("Location", "Place: " + place.getName());
                advertisementLocation.setText(place.getName());
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
                            Toast.makeText(AdvertisementDetailsFormActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                            assert response.body() != null;
                            FileUploadData imgData = response.body().getfileData();
                            if (imgData != null) {
                                fileUploadData.add(imgData.getCustomPath());
                                filepath = imgData.getCustomPath();
                            }
                            Attachments attachments = new Attachments();
                            attachments.setAttachment(filepath);
                            attachmentsList.add(attachments);
                            addNewAdvertisementImagesAdapter.notifyDataSetChanged();
                            m_Dialog.dismiss();
                        } else {
                            Toast.makeText(AdvertisementDetailsFormActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<FileupoladResponse> call, Throwable t) {
                    Log.e("FILE UPLOAD REG", "" + t.getMessage());
                    Toast.makeText(AdvertisementDetailsFormActivity.this, "Failed " + t.getMessage(), Toast.LENGTH_LONG).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AdvertisementDetailsFormActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                    Log.e("OnFailure", "" + t.getMessage());
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
