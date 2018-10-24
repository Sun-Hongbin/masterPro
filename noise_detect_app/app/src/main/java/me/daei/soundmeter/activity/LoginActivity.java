package me.daei.soundmeter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity implements HttpDataResponse {

    private String loginUrl = Urls.t460pUrl + "/user/login";
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private boolean isExit = false;
    private ProgressDialog progressDialog = null;
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
                    Toast.makeText(LoginActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    if ((msg.obj).equals("Login in success")) {
                        finish();
                    }
                    break;
            }

        }
    };

    @BindView(R.id.input_mobile)
    EditText _mobileText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }
//        _loginButton.setEnabled(false);//置为false则点击按钮后不能再点第二次

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        User user = new User();
        String phoneNumber = _mobileText.getText().toString();
        user.setUserPhone(Long.valueOf(phoneNumber));
        String password = _passwordText.getText().toString();
        user.setPassword(password);

        // TODO: Implement your own authentication logic here.
        DoUpload doUpload = new DoUpload();
        doUpload.doUpload_Login(loginUrl
                + "?userPhone=" + phoneNumber + "&password=" + password, this);
        handler.sendEmptyMessageDelayed(0, 1500);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed! No such user Or wrong password.",
                Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        String phoneNumber = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();

        if (phoneNumber.isEmpty() || phoneNumber.length() != 11) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onDataResponse(String res, String phoneNumber) {
        Message message = handler.obtainMessage();
        message.obj = res;
        message.what = 1;
        handler.sendMessageDelayed(message, 2100);
    }

    @Override
    public void onNoReceiveDataResponse(IOException e) {
    }
}
