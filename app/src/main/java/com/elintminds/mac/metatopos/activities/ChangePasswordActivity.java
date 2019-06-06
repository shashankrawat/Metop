package com.elintminds.mac.metatopos.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.login.LoginResponse;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView changepassword_backbtn;
    EditText ed_old_password, ed_new_password, ed_confirm_password;
    Button save_Password;
    Snackbar snackbar;
    View view;
    APIService mApiServices;
    SharedPreferences preferences;
    HashMap<String, String> map = new HashMap<String, String>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        changepassword_backbtn = findViewById(R.id.changepassword_backbtn);
        ed_old_password = findViewById(R.id.ed_old_password);
        ed_new_password = findViewById(R.id.ed_new_password);
        ed_confirm_password = findViewById(R.id.ed_confirm_password);
        save_Password = findViewById(R.id.save_password_btn);
        save_Password.setOnClickListener(this);
        changepassword_backbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == changepassword_backbtn) {
            finish();
        } else if (view == save_Password) {
            if (ed_old_password.getText().toString().equals("") || ed_old_password.getText().toString().length() < 6) {
                snackbar = Snackbar.make(view, "Enter your Old Password", Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(getResources().getColor(R.color.colorred));
                snackbar.show();
            } else if (ed_new_password.getText().toString().equals("") || ed_new_password.getText().toString().length() < 6) {
                snackbar = Snackbar.make(view, "Enter valid password of minimum 6 character with at least 1 capital letter, 1 digit & 1 special character", Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(getResources().getColor(R.color.colorred));
                snackbar.show();
            } else if (ed_confirm_password.getText().toString().equals("") || ed_confirm_password.getText().toString().length() < 6) {
                snackbar = Snackbar.make(view, "Enter valid password of minimum 6 character with at least 1 capital letter, 1 digit & 1 special character", Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(getResources().getColor(R.color.colorred));
                snackbar.show();
            } else if (!ed_new_password.getText().toString().trim().equals(ed_confirm_password.getText().toString().trim())) {
                snackbar = Snackbar.make(view, "New Password and Confirm Password are MisMatched", Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(getResources().getColor(R.color.colorred));
                snackbar.show();
            } else {
                preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
                String token = preferences.getString("User_Token", null);
//                map.put("token",token);
                map.put("oldpassword", ed_old_password.getText().toString().trim());
                map.put("newpassword", ed_new_password.getText().toString().trim());
                map.put("confirmnewpassword", ed_confirm_password.getText().toString());
                changePassword(token, map);
            }
        }
    }

    private void changePassword(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            mApiServices = RetrofitClient.getClient().create(APIService.class);
            Call<LoginResponse> call = mApiServices.changePassword(token, map);
            final ProgressDialog m_Dialog = DialogUtils.showProgressDialog(this);
            m_Dialog.show();
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {

                            Toast.makeText(ChangePasswordActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            ed_old_password.setText("");
                            ed_new_password.setText("");
                            ed_confirm_password.setText("");
                            m_Dialog.dismiss();
                            finish();
                        } else {

                            if (response.body().getError().getOldpassword() != null) {
                                Toast.makeText(ChangePasswordActivity.this, "" + response.body().getError().getOldpassword(), Toast.LENGTH_SHORT).show();
                            } else if (response.body().getError().getNewpassword() != null) {
                                Toast.makeText(ChangePasswordActivity.this, "" + response.body().getError().getNewpassword(), Toast.LENGTH_SHORT).show();
                            } else if (response.body().getError().getConfirmnewpassword() != null) {
                                Toast.makeText(ChangePasswordActivity.this, "" + response.body().getError().getConfirmnewpassword(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            m_Dialog.dismiss();
                        }

                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {

                    Toast.makeText(ChangePasswordActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
