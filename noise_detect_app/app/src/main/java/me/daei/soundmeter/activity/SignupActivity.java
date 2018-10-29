package me.daei.soundmeter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.daei.soundmeter.Entity.Urls;
import me.daei.soundmeter.Entity.User;
import me.daei.soundmeter.R;
import me.daei.soundmeter.service.DoUpload;
import me.daei.soundmeter.service.HttpDataResponse;

public class SignupActivity extends AppCompatActivity implements HttpDataResponse {

    private ProgressDialog progressDialog = null;
    private String registerUrl = Urls.localUrl + "/user/register";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int key = msg.what;
            switch (key) {
                case 0:
                    progressDialog.dismiss();
                    break;
                case 1:
                    if ((msg.obj).equals("register success")) {
                        finish();
                    }
                    Toast.makeText(SignupActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        if (!validate()) {
            System.out.println("!!!参数不合法");
            return;
        }

        progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        User user = new User();
        String name = _nameText.getText().toString();
        user.setName(name);
        String email = _emailText.getText().toString();
        user.setEmail(email);
        String mobile = _mobileText.getText().toString();
        user.setUserPhone(Long.valueOf(mobile));
        String password = _passwordText.getText().toString();
        user.setPassword(password);

        // TODO: Implement your own signup logic here.
        DoUpload doUpload = new DoUpload();
        doUpload.doUpload_Register(user, registerUrl, this);//this指本页面
        handler.sendEmptyMessageDelayed(0, 1200);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 2) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 11) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 12) {
            _passwordText.setError("between 6 and 12 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 6 || reEnterPassword.length() > 120 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onDataResponse(String res, String phoneNumber) {
        Message message = handler.obtainMessage();
        message.obj = res;
        message.what = 1;
        handler.sendMessageDelayed(message, 1250);
    }

    @Override
    public void onNoReceiveDataResponse(IOException e) {

    }
}