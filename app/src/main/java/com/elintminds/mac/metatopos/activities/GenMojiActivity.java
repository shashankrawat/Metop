package com.elintminds.mac.metatopos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.adapters.GenMojiViewPagerAdapter;
import com.elintminds.mac.metatopos.beans.genEmoji.GenEmojiData;

public class GenMojiActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_back_genmoji;
    GenMojiViewPagerAdapter myPagerAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;
    Button btn_save;
    private GenEmojiData selectedData;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_moji);
        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tablayout);
        btn_save = findViewById(R.id.btn_save);
        iv_back_genmoji = findViewById(R.id.iv_back_genmoji);
        myPagerAdapter = new GenMojiViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        iv_back_genmoji.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        intent = new Intent();
    }

    @Override
    public void onClick(View view) {
        if (view == iv_back_genmoji) {
            finish();
        } else if (view == btn_save) {
            if (selectedData != null) {
                intent.putExtra("EMOJI_DATA", selectedData);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

    }

    public void onDataSelect(GenEmojiData selectedData) {
        this.selectedData = selectedData;
        Log.e("DATA", "" + selectedData.getMojiImageUrl() + ", " + selectedData.getId());

    }


}
