package com.elintminds.mac.metatopos.activities.addnewpost;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.icu.util.Calendar;
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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.addnewpost.Adapers.AddNewEventImagesAdapter;
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

public class EventDetailFormActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private static final int STORAGE_PERMISSION_CODE = 201;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 200;
    private static final int CAMERA_REQUEST = 101;
    private static final int GALLARY_REQUEST = 102;
    public static final int requestCode_Location = 80;
    String imagePathNew, picturePath;
    TextInputEditText eventName, event_description, eventStartTime, eventEndTime, eventStartDate, eventEndDate, eventLocation;
    RadioGroup eventVisibility;
    TextView tv_location_address;
    RadioButton visibleEveryone, visibleFollowersOnly;
    Button btnPostevent;
    ImageView event_details_backbtn, uploadEventImages;
    RecyclerView event_images_container_Recyclerview;
    Dialog dialog;
    Calendar mcurrentTime, calendar;
    int hour, minute, second, dayOfMonth, year, month;
    TimePickerDialog mTimePicker;
    DatePickerDialog datePickerDialog;
    LinearLayoutManager linearLayoutManager;
    AddNewEventImagesAdapter addNewPostImagesAdapter;
    ProgressDialog m_Dialog;
    APIService mapiService;
    private String previousUploadFile = "";
    String city, state, country, zipcode, lat, lng, token;
    HashMap<String, Object> map = new HashMap<String, Object>();
    ArrayList<String> fileUploadData = new ArrayList<>();
    SharedPreferences preferences;
    String categoryID, filepath, editPostSuperCategories;
    ArrayList<Attachments> attachmentsList = new ArrayList<>();
    String isFrom, editPostCategories, supreCategoryID, start_date, start_time, end_date, end_time;
    GetPostByIdData postdata;
    private GetLocation getLocation;
    List<Address> addresses = null;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        getLocation = new GetLocation(this);
        checkForPermissionLocation(this);
        mcurrentTime = Calendar.getInstance();
        calendar = Calendar.getInstance();
        hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        minute = mcurrentTime.get(Calendar.MINUTE);
        second = mcurrentTime.get(Calendar.SECOND);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        mapiService = RetrofitClient.getClient().create(APIService.class);
        initviews();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        event_images_container_Recyclerview.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        addNewPostImagesAdapter = new AddNewEventImagesAdapter(getApplicationContext(), fileUploadData);
        event_images_container_Recyclerview.setAdapter(addNewPostImagesAdapter);
        Intent intent = getIntent();
        if (intent != null) {
            categoryID = intent.getStringExtra("ChildCategoryID");
            supreCategoryID = intent.getStringExtra("SuperCATEGORYID");
            Log.e("CategoryID", "" + categoryID);
            postdata = intent.getParcelableExtra("Edit_Post_Data");
            isFrom = intent.getStringExtra("isFromRemoveEditActivity");
        }
        if (isFrom.equals("Category_Event")) {
            eventName.setText("");
            eventStartDate.setText("");
            eventEndDate.setText("");
            event_description.setText("");
            eventLocation.setText("");
            eventEndTime.setText("");
            eventStartTime.setText("");
        } else if (isFrom.equals("Events")) {
            eventName.setText(postdata.getTitle());
            event_description.setText(postdata.getDescription());
//            ed_PostDescription.setText(postdata.get(0).getDescription());
            fileUploadData.add(postdata.getAttachments().get(0).getImageUrl());
            fileUploadData.add(postdata.getAttachments().get(0).getImageUrl());
            addNewPostImagesAdapter = new AddNewEventImagesAdapter(getApplicationContext(), fileUploadData);
            event_images_container_Recyclerview.setAdapter(addNewPostImagesAdapter);
            Attachments attachments = new Attachments();
            attachments.setAttachment(postdata.getAttachments().get(0).getImageUrl());
            attachmentsList.add(attachments);
            addNewPostImagesAdapter.notifyDataSetChanged();
            eventLocation.setText(postdata.getAddress());
            editPostCategories = postdata.getCategoryId();
            editPostSuperCategories = postdata.getSuperCategoryId();
            city = postdata.getCity();
            state = postdata.getState();
            country = postdata.getCountry();
            zipcode = postdata.getZipcode();
            lat = postdata.getLatitude();
            lng = postdata.getLongitude();
            Log.e("Start Date Time", "" + postdata.getStartDateTime());
            Log.e(" End Date Time", "" + postdata.getEndDateTime());
            String startdatetime, endDaettime;
            startdatetime = postdata.getStartDateTime();
            endDaettime = postdata.getEndDateTime();
            StringTokenizer tokens = new StringTokenizer(startdatetime, " ");
            StringTokenizer tokens2 = new StringTokenizer(endDaettime, " ");
            start_date = tokens.nextToken();
            start_time = tokens.nextToken();
            end_date = tokens2.nextToken();
            end_time = tokens2.nextToken();
            eventStartDate.setText(start_date);
            eventStartTime.setText(start_time);
            eventEndDate.setText(end_date);
            eventEndTime.setText(end_time);
            if (postdata.getVisibilityTypeId().equals(0)) {
                visibleFollowersOnly.setChecked(true);
            } else if (postdata.getVisibilityTypeId().equals(1)) {
                visibleEveryone.setChecked(true);
            }

        }
    }

    private void initviews() {
        eventName = findViewById(R.id.event_name);
        event_description = findViewById(R.id.event_Description);
        eventStartDate = findViewById(R.id.event_startDate);
        eventEndDate = findViewById(R.id.event_endDate);
        eventStartTime = findViewById(R.id.event_startTime);
        eventEndTime = findViewById(R.id.event_endTime);
        eventLocation = findViewById(R.id.event_location);
        eventVisibility = findViewById(R.id.visibility_group);
        visibleEveryone = findViewById(R.id.event_visibility_evenyone);
        visibleFollowersOnly = findViewById(R.id.event_visibility_followers);
        btnPostevent = findViewById(R.id.post_Event_btn);
        event_details_backbtn = findViewById(R.id.event_details_backbtn);
        uploadEventImages = findViewById(R.id.iv_add_eventimages);
        tv_location_address = findViewById(R.id.tv_location_address);
        event_images_container_Recyclerview = findViewById(R.id.event_images_container_Recyclerview);
        event_details_backbtn.setOnClickListener(this);
        eventLocation.setOnClickListener(this);
        uploadEventImages.setOnClickListener(this);
        eventEndDate.setOnClickListener(this);
        eventStartTime.setOnClickListener(this);
        eventStartDate.setOnClickListener(this);
        eventEndTime.setOnClickListener(this);
        btnPostevent.setOnClickListener(this);
        eventEndTime.setOnFocusChangeListener(this);
        eventStartTime.setOnFocusChangeListener(this);
        eventStartDate.setOnFocusChangeListener(this);
        eventEndDate.setOnFocusChangeListener(this);
        eventLocation.setOnFocusChangeListener(this);
        tv_location_address.setOnClickListener(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        if (view == event_details_backbtn) {
            finish();
        } else if (view == eventStartDate) {

            datePickerDialog = new DatePickerDialog(EventDetailFormActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int selectedyear, int selectedmonth, int selectedday) {


                            year = selectedyear;
                            month = selectedmonth;
                            dayOfMonth = selectedday;
                            eventStartDate.setText(new StringBuilder().append(month + 1)
                                    .append("-").append(dayOfMonth).append("-")
                                    .append(year).append(" "));

                        }
                    }, year, month, dayOfMonth);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();


        } else if (view == eventEndDate) {

            datePickerDialog = new DatePickerDialog(EventDetailFormActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int selectedyear, int selectedmonth, int selectedday) {


                            year = selectedyear;
                            month = selectedmonth;
                            dayOfMonth = selectedday;
                            eventEndDate.setText(new StringBuilder().append(month + 1)
                                    .append("-").append(dayOfMonth).append("-")
                                    .append(year).append(" "));

                        }
                    }, year, month, dayOfMonth);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();

        } else if (view == eventStartTime) {
            mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    eventStartTime.setText(selectedHour + ":" + selectedMinute + ":00");
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        } else if (view == eventEndTime) {
            mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    eventEndTime.setText(selectedHour + ":" + selectedMinute + ":00");
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();


        } else if (view == eventLocation) {
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

        } else if (view == uploadEventImages) {
            if (ContextCompat.checkSelfPermission(EventDetailFormActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                showImagePickerDialog();
            } else {
                requestStoragePermission();
            }

        } else if (view == btnPostevent) {
            if (eventName.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.enter_name, Toast.LENGTH_SHORT).show();
                eventName.requestFocus();
            } else if (event_description.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.enter_description, Toast.LENGTH_SHORT).show();
                event_description.requestFocus();
            } else if (eventStartDate.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.enter_start_date, Toast.LENGTH_SHORT).show();
                eventStartDate.requestFocus();
            } else if (eventStartTime.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.enter_start_time, Toast.LENGTH_SHORT).show();
                eventStartTime.requestFocus();
            } else if (eventEndDate.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.enter_end_date, Toast.LENGTH_SHORT).show();
                eventEndDate.requestFocus();
            } else if (eventEndTime.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.enter_end_time, Toast.LENGTH_SHORT).show();
                eventEndTime.requestFocus();
            } else if (eventLocation.getText().toString().trim().equals("")) {
                Toast.makeText(this, R.string.enter_location, Toast.LENGTH_SHORT).show();
                eventLocation.requestFocus();
            } else if (addNewPostImagesAdapter.getItemCount() == 0) {
                Toast.makeText(this,R.string.upload_atleast_one_image, Toast.LENGTH_SHORT).show();

            } else {
                if (isFrom.equals("Category_Event")) {
                    String startDateTime = eventStartDate.getText().toString() + " " + eventStartTime.getText().toString();
                    String endDateTime = eventEndDate.getText().toString() + " " + eventEndTime.getText().toString();
                    map.put("categoryId", "");
                    map.put("postid", "");
                    map.put("superCategoryId", supreCategoryID);
                    map.put("title", eventName.getText().toString());
                    map.put("description", event_description.getText().toString());
                    if (visibleEveryone.isChecked() == true) {
                        map.put("visibilityTypeId", "1");
                    } else if (visibleFollowersOnly.isChecked() == true) {
                        map.put("visibilityTypeId", "2");
                    }
                    map.put("address", eventLocation.getText().toString());
                    map.put("city", city);
                    map.put("state", state);
                    map.put("country", country);
                    map.put("zipcode", zipcode);
                    map.put("latitude", lat);
                    map.put("longitude", lng);
                    map.put("attachments", attachmentsList);
                    map.put("startDateTime", startDateTime);
                    map.put("endDateTime", endDateTime);
                    preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
                    token = preferences.getString("User_Token", null);
                    saveNewPost(token, map);
                } else if (isFrom.equals("Events")) {
                    String startDateTime = eventStartDate.getText().toString() + " " + eventStartTime.getText().toString();
                    String endDateTime = eventEndDate.getText().toString() + " " + eventEndTime.getText().toString();
                    map.put("postid", postdata.getPostId());
                    map.put("categoryId", "");
                    map.put("superCategoryId", editPostSuperCategories);
                    map.put("title", eventName.getText().toString());
                    map.put("description", event_description.getText().toString());
                    if (visibleEveryone.isChecked() == true) {
                        map.put("visibilityTypeId", "1");
                    } else if (visibleFollowersOnly.isChecked() == true) {
                        map.put("visibilityTypeId", "2");
                    }
                    map.put("address", eventLocation.getText().toString());
                    map.put("city", city);
                    map.put("state", state);
                    map.put("country", country);
                    map.put("zipcode", zipcode);
                    map.put("latitude", lat);
                    map.put("longitude", lng);
                    map.put("attachments", attachmentsList);
                    map.put("startDateTime", startDateTime);
                    map.put("endDateTime", endDateTime);
                    preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
                    token = preferences.getString("User_Token", null);
                    saveNewPost(token, map);
                }
            }
        } else if (view == tv_location_address) {
            eventLocation.setText(city);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onFocusChange(View focusview, boolean hasFocus) {
        if (focusview == eventEndDate) {
            if (hasFocus) {
                datePickerDialog = new DatePickerDialog(EventDetailFormActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int selectedyear, int selectedmonth, int selectedday) {


                                year = selectedyear;
                                month = selectedmonth;
                                dayOfMonth = selectedday;
                                eventStartDate.setText(new StringBuilder().append(month + 1)
                                        .append("-").append(dayOfMonth).append("-")
                                        .append(year).append(" "));

                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        } else if (focusview == eventStartDate) {
            datePickerDialog = new DatePickerDialog(EventDetailFormActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int selectedyear, int selectedmonth, int selectedday) {


                            year = selectedyear;
                            month = selectedmonth;
                            dayOfMonth = selectedday;
                            eventStartDate.setText(new StringBuilder().append(month + 1)
                                    .append("-").append(dayOfMonth).append("-")
                                    .append(year).append(" "));

                        }
                    }, year, month, dayOfMonth);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        } else if (focusview == eventStartTime) {
            if (hasFocus) {
                mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        eventStartTime.setText(selectedHour + ":" + selectedMinute + ":00");
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }

        } else if (focusview == eventEndTime) {
            if (hasFocus) {
                mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        eventEndTime.setText(selectedHour + ":" + selectedMinute + ":00");
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }

        } else if (focusview == eventLocation) {
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


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            ActivityCompat.requestPermissions(EventDetailFormActivity.this,
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
                eventLocation.setText(place.getName());

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
                            Toast.makeText(EventDetailFormActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                            assert response.body() != null;
                            FileUploadData imgData = response.body().getfileData();
                            if (imgData != null) {
                                fileUploadData.add(imgData.getCustomPath());
                                filepath = imgData.getCustomPath();
                            }
                            Attachments attachments = new Attachments();
                            attachments.setAttachment(filepath);
                            attachmentsList.add(attachments);
                            addNewPostImagesAdapter.notifyDataSetChanged();

                            m_Dialog.dismiss();
                        } else {
                            Toast.makeText(EventDetailFormActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<FileupoladResponse> call, Throwable t) {
                    Log.e("FILE UPLOAD REG", "" + t.getMessage());
                    Toast.makeText(EventDetailFormActivity.this, "Failed " + t.getMessage(), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(EventDetailFormActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                    showImagePickerDialog();
                } else {
                    Toast.makeText(this, R.string.permission_dined, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
