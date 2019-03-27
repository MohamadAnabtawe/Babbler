package com.android.babbler.Manager;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteUserRequest extends  StringRequest {
        private static final String URL = "http://mohamadka.000webhostapp.com/DeleteUser.php";
        private Map<String, String> params;

        public DeleteUserRequest(int ID, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            params = new HashMap<>();
            params.put("id", ID+"");
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
}
