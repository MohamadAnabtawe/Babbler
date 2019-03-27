package com.android.babbler.LoginRegister;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.babbler.R;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity  {

    private static ProgressDialog progressDialog;
    public static String role="participant";
    EditText et_email, et_password,et_name,et_address,et_phone,et_bdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);
        et_name = findViewById(R.id.name);
        et_address = findViewById(R.id.address);
        et_phone = findViewById(R.id.phone);
        et_bdate = findViewById(R.id.bdate);
        TextView textView=findViewById(R.id.tv_signup);
        if(role.equals("moderator"))
            textView.setText("Moderator Registration");
        else if(role.equals("manager"))
            textView.setText("Manager Registration");

        progressDialog = new ProgressDialog(this);
    }

    public void onClick(View view)
    {
        final String email = et_email.getText().toString();
        final String password = et_password.getText().toString();
        final String name = et_name.getText().toString();
        final String bdate = et_bdate.getText().toString();
        final String address = et_address.getText().toString();
        final String phone = et_phone.getText().toString();
        if(name.isEmpty()){
            Toast.makeText(getApplicationContext(),"insert your name",Toast.LENGTH_SHORT).show();
            return;
        }
         if(email.isEmpty()){
            Toast.makeText(getApplicationContext(),"insert your email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.isEmpty()){
            Toast.makeText(getApplicationContext(),"insert your password",Toast.LENGTH_SHORT).show();
            return;
        }
        if(phone.isEmpty()){
            Toast.makeText(getApplicationContext(),"insert your phone number",Toast.LENGTH_SHORT).show();
            return;
        }
        if(bdate.isEmpty()){
            Toast.makeText(getApplicationContext(),"insert your birthday",Toast.LENGTH_SHORT).show();
            return;
        }
        if(address.isEmpty()){
            Toast.makeText(getApplicationContext(),"insert your address",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering....");
        progressDialog.setTitle("");
        progressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Thank you "+name+" for registering",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("Register Failed")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(role, name,email, bdate, password, phone, address ,responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);
    }
    private class RegisterRequest extends StringRequest {

        private static final String REGISTER_REQUEST_URL = "https://mohamadka.000webhostapp.com/connect/Register.php";
        private Map<String, String> params;

        public RegisterRequest(String role, String name, String email , String bdate, String password, String phonenumber , String address, Response.Listener<String> listener) {
            super(Method.POST, REGISTER_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("role", role);
            params.put("name", name);
            params.put("email", email);
            params.put("bdate", bdate);
            params.put("password", password);
            params.put("phone", phonenumber);
            params.put("address", address);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

 }



