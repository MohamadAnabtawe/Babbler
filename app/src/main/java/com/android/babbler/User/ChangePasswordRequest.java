package com.android.babbler.User;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alonka on 04-Jan-17.
 */

public class ChangePasswordRequest extends StringRequest {
    private static final String CHANGE_PASSWORD_REQUEST_URL = "https://mohamadka.000webhostapp.com/ChangePassword.php";
    private Map<String, String> params;

    public ChangePasswordRequest(String user_id, String new_password, Response.Listener<String> listener) {
        super(Method.POST, CHANGE_PASSWORD_REQUEST_URL, listener, null);
        params = new HashMap<>();

        params.put ("user_id", user_id);
        params.put ("password", new_password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
