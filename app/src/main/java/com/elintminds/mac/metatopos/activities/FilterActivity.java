package com.elintminds.mac.metatopos.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.getfiltersCategoryList.PostFilterData;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.GetLocation;

import java.util.HashMap;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int requestCode_Location = 80;
    HashMap<String, String> map;
    APIService mapiService;
    ProgressDialog m_Dialog;
    private SharedPreferences preferences;
    RelativeLayout post_lay;
    LinearLayout price_lay;
    ImageView filterpost_back_btn, post_view, event_view, advertisement_view;
    AppCompatSeekBar rangeOnMapSeekbar;
    TextView rangebarValue, eventStartdate, eventEndDate, set_price;
    Button btn_ApplyFilter, btn_ClearFilter;
    int maxX;
    PostFilterData filterData;
    EditText min_price, max_price;
    private String token, categoryID;
    private GetLocation getLocation;
    double currentLat, curentLng;
    Calendar mcurrentTime, calendar;
    int hour, minute, dayOfMonth, year, month;
    TimePickerDialog mTimePicker;
    DatePickerDialog datePickerDialog;
    boolean isPostSelect = false, isEventSelected = false, isAdvertisemntSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getLocation = new GetLocation(this);
        intiViews();
    }

    private void intiViews() {
        post_lay = findViewById(R.id.post_lay);
        filterpost_back_btn = findViewById(R.id.filterpost_back_btn);
        rangeOnMapSeekbar = findViewById(R.id.rangeonmap_seekbar);
        rangebarValue = findViewById(R.id.showseekbarrange);
        post_view = findViewById(R.id.postPic_lay);
        event_view = findViewById(R.id.event_Pic);
        advertisement_view = findViewById(R.id.Advertisement_Pic);
        max_price = findViewById(R.id.max_price);
        min_price = findViewById(R.id.min_price);
        set_price = findViewById(R.id.set_price);
        eventStartdate = findViewById(R.id.event_startDate);
        eventEndDate = findViewById(R.id.event_enddate);
        price_lay = findViewById(R.id.price_lay);
        btn_ApplyFilter = findViewById(R.id.btn_Apply_Filter);
        btn_ClearFilter = findViewById(R.id.btn_clear_Filter);
        event_view.setOnClickListener(this);
        post_view.setOnClickListener(this);
        advertisement_view.setOnClickListener(this);
        filterpost_back_btn.setOnClickListener(this);
        btn_ApplyFilter.setOnClickListener(this);
        eventStartdate.setOnClickListener(this);
        eventEndDate.setOnClickListener(this);
        btn_ClearFilter.setOnClickListener(this);
        rangeOnMapSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rangebarValue.setText("" + rangeOnMapSeekbar.getProgress());
                Point maxSizePoint = new Point();
                getWindowManager().getDefaultDisplay().getSize(maxSizePoint);
                maxX = maxSizePoint.x;
                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                rangebarValue.setText("" + progress + "km");
                int textViewX = val - (rangebarValue.getWidth() / 2);
                int finalX = rangebarValue.getWidth() + textViewX > maxX ? (maxX - rangebarValue.getWidth() - 16) : textViewX + 16/*your margin*/;
                rangebarValue.setX(finalX < 0 ? 16/*your margin*/ : finalX);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {

        if (view == post_view) {
            event_view.setBackgroundResource(R.drawable.filterselectpostbggray);
            advertisement_view.setBackgroundResource(R.drawable.filterselectpostbggray);
            post_view.setBackgroundResource(R.drawable.filterselectpostbg);
            max_price.setVisibility(View.VISIBLE);
            min_price.setVisibility(View.VISIBLE);
            price_lay.setVisibility(View.VISIBLE);
            eventEndDate.setVisibility(View.GONE);
            eventStartdate.setVisibility(View.GONE);
            set_price.setText(R.string.Set_price_range);
            Intent i = new Intent(this, SelectPostForFilterActivity.class);
            startActivityForResult(i, 1000);
            isPostSelect = true;
            isEventSelected = false;
            isAdvertisemntSelected = false;
        } else if (view == event_view) {
            event_view.setBackgroundResource(R.drawable.filterselectpostbg);
            advertisement_view.setBackgroundResource(R.drawable.filterselectpostbggray);
            post_view.setBackgroundResource(R.drawable.filterselectpostbggray);
            max_price.setVisibility(View.GONE);
            min_price.setVisibility(View.GONE);
            eventEndDate.setVisibility(View.VISIBLE);
            eventStartdate.setVisibility(View.VISIBLE);
            price_lay.setVisibility(View.VISIBLE);
            set_price.setText(R.string.select_start_date_to_end_date);
            isPostSelect = false;
            isEventSelected = true;
            isAdvertisemntSelected = false;
            filterData = new PostFilterData();
            filterData.setValue("6");
        } else if (view == advertisement_view) {
            event_view.setBackgroundResource(R.drawable.filterselectpostbggray);
            advertisement_view.setBackgroundResource(R.drawable.filterselectpostbg);
            post_view.setBackgroundResource(R.drawable.filterselectpostbggray);
            price_lay.setVisibility(View.INVISIBLE);
            set_price.setVisibility(View.INVISIBLE);
            isPostSelect = false;
            isEventSelected = false;
            isAdvertisemntSelected = true;
            filterData = new PostFilterData();
            filterData.setValue("7");
        } else if (view == filterpost_back_btn) {
            finish();
        } else if (view == eventStartdate) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(FilterActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            int m = month + 1;
                            eventStartdate.setText(year + "-" + m + "-" + day);
                        }
                    }, year, month, dayOfMonth);
            datePickerDialog.show();

        } else if (view == eventEndDate) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(FilterActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            int m = month + 1;
                            eventEndDate.setText(year + "-" + m + "-" + day);
                        }
                    }, year, month, dayOfMonth);
            datePickerDialog.show();
        } else if (view == btn_ApplyFilter) {
            Log.e("Seekbar_Value", "" + rangeOnMapSeekbar.getProgress());
            String rangeonmap = String.valueOf(rangeOnMapSeekbar.getProgress());
            Intent intent = new Intent();
            if (isPostSelect == true && isAdvertisemntSelected == false && isEventSelected == false) {
                intent.putExtra("RangeValue", rangeonmap);
                intent.putExtra("MaxPrice", max_price.getText().toString());
                intent.putExtra("MinPrice", min_price.getText().toString());
                intent.putExtra("CategoryValue", filterData);
                intent.putExtra("IsFrom", "ApplyFiltter");
                setResult(RESULT_OK, intent);
                finish();
            } else if (isPostSelect == false && isAdvertisemntSelected == false && isEventSelected == true) {
                intent.putExtra("RangeValue", rangeonmap);
                intent.putExtra("StartData", eventStartdate.getText().toString());
                intent.putExtra("EndDate", eventEndDate.getText().toString());
                intent.putExtra("CategoryValue", filterData);
                intent.putExtra("IsFrom", "ApplyFiltter");
                setResult(RESULT_OK, intent);
                finish();
            } else if (isPostSelect == false && isAdvertisemntSelected == true && isEventSelected == false) {

                intent.putExtra("RangeValue", rangeonmap);
                intent.putExtra("MaxPrice", max_price.getText().toString());
                intent.putExtra("MinPrice", min_price.getText().toString());
                intent.putExtra("CategoryValue", filterData);
                intent.putExtra("IsFrom", "ApplyFiltter");
                setResult(RESULT_OK, intent);
                finish();
            }
        } else if (view == btn_ClearFilter) {
//            Intent intent = new Intent();
//            setResult(RESULT_OK, intent);
//            intent.putExtra("IsFrom", "ClearFiltter");
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK && data != null) {
                filterData = data.getParcelableExtra("FILTER_DATA");
            }
        }
    }
}
