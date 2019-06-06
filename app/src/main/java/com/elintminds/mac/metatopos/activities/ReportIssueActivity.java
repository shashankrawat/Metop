package com.elintminds.mac.metatopos.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.login.ReportIssueResponse;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportIssueActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView report_issue_backbtn;
    APIService mapiService;
    ProgressDialog m_Dialog;
    SharedPreferences preferences;
    Button submit_issueBtn;
    EditText ed_title, ed_message;
    Snackbar snackbar;
    View view;
    HashMap<String, String> map = new HashMap<String, String>();
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);
        report_issue_backbtn = findViewById(R.id.report_issue_backbtn);
        ed_title = findViewById(R.id.ed_title);
        ed_message = findViewById(R.id.ed_message);
        submit_issueBtn = findViewById(R.id.submit_issue);
        submit_issueBtn.setOnClickListener(this);
        report_issue_backbtn.setOnClickListener(this);
        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);

    }

    @Override
    public void onClick(View view) {
        if (view == report_issue_backbtn) {
            finish();
        } else if (view == submit_issueBtn) {
            if (ed_title.getText().toString().equals("")) {
                snackbar = Snackbar.make(view, "Please enter Title", Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(getResources().getColor(R.color.colorred));
                snackbar.show();
            } else if (ed_message.getText().toString().equals("")) {
                snackbar = Snackbar.make(view, "Please enter Message", Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(getResources().getColor(R.color.colorred));
                snackbar.show();
            } else {
//                map.put("token",token);
                map.put("title", ed_title.getText().toString());
                map.put("message", ed_message.getText().toString());
                saveReportIssue(token, map);
            }
        }
    }

    private void saveReportIssue(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<ReportIssueResponse> call = mapiService.reportIssue(token, map);
            m_Dialog = DialogUtils.showProgressDialog(ReportIssueActivity.this);
            m_Dialog.show();
            call.enqueue(new Callback<ReportIssueResponse>() {
                @Override
                public void onResponse(Call<ReportIssueResponse> call, Response<ReportIssueResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            Log.e("Submit Issue", "" + response.body().getMessage());
                            ed_title.setText("");
                            ed_message.setText("");
                            Toast.makeText(ReportIssueActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            m_Dialog.dismiss();
                        } else {

                            Toast.makeText(ReportIssueActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }

                }

                @Override
                public void onFailure(Call<ReportIssueResponse> call, Throwable t) {
                    Log.e("Report_issue", "" + t.getMessage());
                    Toast.makeText(ReportIssueActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();

                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
