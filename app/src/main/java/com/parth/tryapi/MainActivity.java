package com.parth.tryapi;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.parth.tryapi.R.id.get;
import static com.parth.tryapi.R.id.resP;

public class MainActivity extends AppCompatActivity {

    private TextView res, resp;
    File file = new File("/storage/emulated/0/Android/data/com.example.speechaid/files/A.mp3");
    public String strFileName = file.getName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button get = findViewById(R.id.get);
        Button post = findViewById(R.id.post);
        res = findViewById(R.id.res);
        resp = findViewById(R.id.resP);

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendGetRequest();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    postRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void postRequest() throws IOException {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        // String url = "http://192.168.43.126/xorai_apk/post.php";
        String url = "http://192.168.43.6:5000/xorai_autox/post_string";

        //BytconeArrayFromAudio();
        convertAudioToByteArray(strFileName);

        JSONObject js = new JSONObject();
        try {
            // js.put("id", "1");
            js.put("data", "xorai");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj = new JSONObject(response.toString());
                            //  String id = obj.getString("id");
                            String data = obj.getString("your data");
                            //String data = "Response: " + id+data ;
                            String data1 = " Data:" + data;

                            resp.setText(data1);
                        } catch (JSONException e) {
                            resp.setText(e.toString());
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //  String TAG = new String();
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hideProgressDialog();
                //  resp.setText("Response: Failed!");
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjReq);


    }


    private void sendGetRequest() {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://192.168.43.6:5000/xorai_autox/test";
        //String url = "http://192.168.43.137/xorai_apk/get.php";
        //  StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject obj = new JSONObject(response.toString());
                            //  String id = obj.getString("id");
                            String data = obj.getString("test data");
                            //String data = "Response: " + id+data ;
                            String data1 = " Data:" + data;

                            res.setText(data1);
                        } catch (JSONException e) {
                            res.setText(e.toString());
                        }
                        // res.setText("Response: " + response.toString());
                    }
//
//                   @Override
//          public void onResponse(String response) {
//            res.setText("Data : "+response);
//           }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        res.setText("Response : RESPONSE FAILED!");
                    }
                });
        queue.add(jsonObjectRequest);
        //      queue.add(stringRequest);
    }
    public byte[] convertAudioToByteArray(String path) throws IOException {

        FileInputStream fis = new FileInputStream(path);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];

        for (int readNum; (readNum = fis.read(b)) != -1;) {
            bos.write(b, 0, readNum);
        }

        byte[] bytes = bos.toByteArray();

        return bytes;
    }

}