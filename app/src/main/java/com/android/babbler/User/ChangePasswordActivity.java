package com.android.babbler.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.babbler.DataClasses.User;
import com.android.babbler.R;
import com.android.babbler.Participant.UserMainActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText etOldPass,etNewPass, etConfirmPass;
    String oldPass, newPass, confirmPass, m_userID, m_password;
    Button confirm;
    private static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        etOldPass =  findViewById(R.id.oldPass);
        etNewPass =  findViewById(R.id.newPass);
        etConfirmPass = findViewById(R.id.confirmPass);
        confirm = findViewById(R.id.changeConfirm);
        m_userID = User.getInstance().getM_id() + "";
        m_password = User.getInstance().getM_password();
    }
    public void onCancel(View view)
    {
        finish();
    }
    public void onConfirm(View view) {

        oldPass = etOldPass.getText().toString();
        newPass = etNewPass.getText().toString();
        confirmPass = etConfirmPass.getText().toString();

        //check if the old password is true and the new password equals the confirm password
        if ((oldPass.equals(m_password)) && (newPass.equals(confirmPass))) {
            ChangePass();
            //if old password is wrong show the corresponding message
        } else if (!(oldPass.equals(m_password))) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("wrong old password").setCancelable(false);
            dlgAlert.setTitle("error");
            dlgAlert.setPositiveButton("try again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            etOldPass.setText("");
                        }
                    });
            dlgAlert.create().show();

            //if confirm password doesn't match the new password show the corresponding message
        } else if (!(newPass.equals(confirmPass))) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("new password does not match confirm password").setCancelable(false);
            dlgAlert.setTitle("error");
            dlgAlert.setPositiveButton("try again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            etNewPass.setText("");
                            etConfirmPass.setText("");
                        }
                    });
            dlgAlert.create().show();
        }
    }
    private void ChangePass()
    {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        User.getInstance().setM_password(newPass);
                        Toast.makeText(ChangePasswordActivity.this, "password changed",
                                Toast.LENGTH_LONG).show();
                        intent = new Intent(ChangePasswordActivity.this, UserMainActivity.class);
                        ChangePasswordActivity.this.startActivity(intent);

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                        builder.setMessage("password change failed")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(m_userID, newPass, responseListener);
        RequestQueue queue = Volley.newRequestQueue(ChangePasswordActivity.this);
        queue.add(changePasswordRequest);
    }
}
