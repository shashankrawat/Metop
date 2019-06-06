package com.elintminds.mac.metatopos.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_Forgot_Password, tv_register_here;
    EditText ed_Username_email, ed_password;
    Button btn_Login;
    Snackbar snackbar;
    APIService mApiServices;
    View view;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    HashMap<String, String> map = new HashMap<String, String>();
    SharedPreferences preferences;
    String device_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ed_Username_email = findViewById(R.id.ed_Email_Username);
        ed_password = findViewById(R.id.ed_Password);
        btn_Login = findViewById(R.id.btn_login);
        tv_Forgot_Password = findViewById(R.id.tv_forgot_password);
        tv_register_here = findViewById(R.id.tv_register_here);
        tv_Forgot_Password.setOnClickListener(this);
        tv_register_here.setOnClickListener(this);
        btn_Login.setOnClickListener(this);

        ed_Username_email.setText("emtalent6");
        ed_password.setText("Qwerty@123");

        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
        device_token = preferences.getString("LOGGED_DEVICE_Token", null);
        Log.e("FCM_TOKEN", "" + device_token);
    }

    @Override
    public void onClick(View view) {
        if (view == btn_Login) {
            if (ed_Username_email.getText().toString().equals("")) {
                snackbar = Snackbar.make(view, R.string.validate_username, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(getResources().getColor(R.color.colorred));
                snackbar.show();
            } else if (ed_password.getText().toString().equals("") || ed_password.getText().toString().length() < 8 || !ed_password.getText().toString().matches(PASSWORD_PATTERN)) {
                snackbar = Snackbar.make(view, R.string.validate_password, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(getResources().getColor(R.color.colorred));
                TextView textView = (TextView) view.findViewById(R.id.snackbar_text);
                textView.setLines(3);
                snackbar.show();
            } else {
                map.put("email", ed_Username_email.getText().toString());
                map.put("password", ed_password.getText().toString());
                map.put("devicetoken", device_token);


                if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
                    doLogin(map);
                } else {
                    Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (view == tv_Forgot_Password) {
            Intent forgotpass = new Intent(this, ForgotPasswordActivity.class);
            startActivity(forgotpass);
        } else if (view == tv_register_here) {
            Intent intent_reg = new Intent(this, RegisterActivity.class);
            startActivity(intent_reg);
            finish();
        }
    }

    void doLogin(HashMap<String, String> map) {
        mApiServices = RetrofitClient.getClient().create(APIService.class);
        Call<LoginResponse> call = mApiServices.LoginResponse(map);
        final ProgressDialog m_Dialog = DialogUtils.showProgressDialog(this);
        m_Dialog.show();
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == true) {
                        String gettoken = response.body().getData().getToken();
                        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("User_Token", gettoken);
                        editor.putString("LOGGED_UDERID", response.body().getData().getUserID());
                        editor.putBoolean("Logged", true);
                        editor.commit();
                        Intent login = new Intent(SigninActivity.this, HomeActivity.class);
                        startActivity(login);
                        finish();
                        m_Dialog.dismiss();
                    } else {
                        Toast.makeText(SigninActivity.this, "" + response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                        m_Dialog.dismiss();
                    }

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(SigninActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                m_Dialog.dismiss();
            }
        });
    }

}

//    !ed_password.getText().toString().matches(PASSWORD_PATTERN)
