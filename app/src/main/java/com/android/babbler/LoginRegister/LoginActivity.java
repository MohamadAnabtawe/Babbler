package com.android.babbler.LoginRegister;


import android.app.ProgressDialog;
import android.content.Intent;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;


import com.android.babbler.DataClasses.User;
import com.android.babbler.Manager.ManagerMainActivity;
import com.android.babbler.DataClasses.Notification;
import com.android.babbler.Moderator.ModeratorMainActivity;
import com.android.babbler.R;
import com.android.babbler.Participant.UserMainActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
//import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    EditText et_email, et_password;
    LoginButton fb_login;
    CallbackManager callbackManager;
    private static ProgressDialog progressDialog;
    static private LoginActivity instance = null;

    public static LoginActivity getInstance() {
        if (instance == null)
            instance = new LoginActivity();
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        AccessToken accessToken=AccountKit.getCurrentAccessToken();
        com.facebook.accountkit.AccessToken loginToken = com.facebook.accountkit.AccountKit.getCurrentAccessToken();
        setContentView(R.layout.activity_login);
        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.pass);
        progressDialog = new ProgressDialog(this);
        fb_login=findViewById(R.id.loginButton);
        fb_login.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        callbackManager= CallbackManager.Factory.create();
        fb_login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                User user = User.getInstance();
                                // Application code
                                try {
                                    String id = object.getString("id");
                                    user.setM_name(id);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    String name = object.getString("name");
                                    user.setM_name(name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    String email = object.getString("email");
                                    user.setM_email(email);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    String birthday = object.getString("birthday");
                                    user.setM_birthday(birthday);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
                Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                LoginActivity.this.startActivity(intent);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "insert your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "insert your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Signing in...");
                progressDialog.setTitle("");
                progressDialog.show();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        User user = User.getInstance();
                        if (user.isConnected()) {
                            Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                            LoginActivity.this.startActivity(intent);
                            finish();
                        } else {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    progressDialog.dismiss();
                                    //get the user's informations
                                    int user_id = jsonResponse.getInt("ID");
                                    String user_role = jsonResponse.getString("role");
                                    String user_name = jsonResponse.getString("name");
                                    String user_email = jsonResponse.getString("email");
                                    String user_password = jsonResponse.getString("password");
                                    String user_phone_number = jsonResponse.getString("phone");
                                    String user_birthday = jsonResponse.getString("bdate");
                                    String user_address = jsonResponse.getString("address");
                                    int user_imageID=jsonResponse.getInt("ImageID");
                                    //set the logged in user information
                                    user.setM_id(user_id);
                                    user.setM_role(user_role);
                                    user.setM_name(user_name);
                                    user.setM_email(user_email);
                                    user.setM_password(user_password);
                                    user.setM_birthday(user_birthday);
                                    user.setM_phone_number(user_phone_number);
                                    user.setM_address(user_address);
                                    user.setM_image(user_imageID);
                                    user.setConnected(true);
                                    Toast.makeText(LoginActivity.this, "Wellcome " + user_name,
                                            Toast.LENGTH_SHORT).show();
                                    Notification notify = new Notification();
                                    notify.notifyMessage("Babbler", "Wellcome Back " + user_name, LoginActivity.this);
                                    //if student open the student's main activity
                                    if (user_role.equals ("participant"))
                                    {
                                        Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                                        LoginActivity.this.startActivity(intent);
                                        finish();
                                    }
                                    else if(user_role.equals ("manager")){
                                        Intent intent = new Intent(LoginActivity.this, ManagerMainActivity.class);
                                        LoginActivity.this.startActivity(intent);
                                        finish();
                                    }
                                    else if(user_role.equals ("moderator")){
                                        Intent intent = new Intent(LoginActivity.this, ModeratorMainActivity.class);
                                        LoginActivity.this.startActivity(intent);
                                        finish();
                                    }
                                }
                                //invalid login (email and password doesn't match)
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("Invalid login!");
                                    builder.setNegativeButton("Retry", null);
                                    builder.create();
                                    builder.show();
                                    progressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                //sends a login request using LoginRequest class
                LoginRequest loginRequest = new LoginRequest(email, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
                break;//end login

            case R.id.sign:
                //go to registration activity
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    private class LoginRequest extends StringRequest {

        private static final String LOGIN_REQUEST_URL = "https://mohamadka.000webhostapp.com/connect/login.php";
        public static final String PASSWORD = "password";
        public static final String EMAIL = "email";
        private Map<String, String> params;

        public LoginRequest(String email, String password, Response.Listener<String> listener) {
            super(Method.POST, LOGIN_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put(EMAIL, email);
            params.put(PASSWORD, password);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
}
