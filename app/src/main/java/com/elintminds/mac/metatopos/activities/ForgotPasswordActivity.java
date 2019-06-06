package com.elintminds.mac.metatopos.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.forgotPassword.ForgotPasswordResponse;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    EditText ed_email;
    Button send_btn;
    APIService mapiService;
    ImageView forgotPassword_backbtn;
    String sendmail;
    ProgressDialog progress;
    HashMap<String, String> map = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ed_email = findViewById(R.id.ed_email);
        send_btn = findViewById(R.id.btn_send);
        forgotPassword_backbtn = findViewById(R.id.forgotPassword_backbtn);
        send_btn.setOnClickListener(this);
        forgotPassword_backbtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view == send_btn) {
            if (!ed_email.getText().toString().trim().matches(EMAIL_PATTERN) || ed_email.getText().toString().equals("")) {
                Snackbar snackbar = Snackbar.make(view, "\nPlease enter valid EmailID ", Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(getResources().getColor(R.color.colorred));
                snackbar.show();
            } else {
                map.put("email", ed_email.getText().toString());

                if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
                    forgotPassword(map);
                } else {
                    Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
            }

        } else if (view == forgotPassword_backbtn) {
            finish();
        }

    }

    private void forgotPassword(HashMap<String, String> map) {
        mapiService = RetrofitClient.getClient().create(APIService.class);
        Call<ForgotPasswordResponse> call = mapiService.ForgotResponse(map);
        final ProgressDialog m_Dialog = DialogUtils.showProgressDialog(this);
        m_Dialog.show();
        call.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == true) {

                        Toast.makeText(ForgotPasswordActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    m_Dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "" + t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                m_Dialog.dismiss();
            }
        });


    }
}
