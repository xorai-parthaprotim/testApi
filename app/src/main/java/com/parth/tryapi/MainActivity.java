package com.parth.tryapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.VolleyLog;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static com.parth.tryapi.R.id.get;
import static com.parth.tryapi.R.id.resP;

public class MainActivity extends AppCompatActivity {

    private TextView res, resp;
    private String recordPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private int PERMISSION_CODE = 101;



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
                // sendGetRequest();
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
        final MediaType MEDIA_TYPE_OCTET = MediaType
                .parse("application/octet-stream;");


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        checkPermissions();
        File file = new File("storage/emulated/0/Android/data/com.example.speechaid/files/A.mp3");
        Uri uri = Uri.fromFile(file);

        String stringUri;
        stringUri = uri.toString();

        uploadFileNew(stringUri);
        resp.setText("File Upload Complete");
    }

    public boolean uploadFileNew(String sourceFileUri) {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File upFile = null;

        try {
            final File root = new File("storage/emulated/0/Android/data/com.example.speechaid/files");


            root.mkdirs();
            final String tmp[] = sourceFileUri.split(File.separator);
            final String fname = tmp[tmp.length - 1];
            upFile = new File(root, fname);


            FileInputStream fileInputStream = new FileInputStream(upFile);


            URL url = new URL("http://192.168.43.6:5000/xorai_autox/messages");
            connection = (HttpURLConnection) url.openConnection();

            // Allow Inputs & Outputs.
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // Set HTTP method to POST.
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");

            connection.setRequestProperty("Content-Type", "application/octet-stream;boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);

            outputStream.writeBytes("Content-Disposition: octet-stream; typ=\"" + 1 + "\"; name=\"" + fname + "\";filename=\"" + fname + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();
            InputStream in = connection.getInputStream();

            byte data[] = new byte[1024];
            int counter = -1;

            in.close();


            if (serverResponseCode == 200) {
                Log.d("uploadFile", "File Upload Complete.");
            }

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

        }
        catch(Exception ex)

        {
            Log.d("uploadFile", "File Upload Error:" + ex.toString());

            return false;

        }


        return true;
    }

//    private void sendGetRequest() {
//        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
//        String url = "http://192.168.43.6:5000/xorai_autox/test";
//        //String url = "http://192.168.43.137/xorai_apk/get.php";
//        //  StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        try {
//                            JSONObject obj = new JSONObject(response.toString());
//                            //  String id = obj.getString("id");
//                            String data = obj.getString("test data");
//                            //String data = "Response: " + id+data ;
//                            String data1 = " Data:" + data;
//
//                            res.setText(data1);
//                        } catch (JSONException e) {
//                            res.setText(e.toString());
//                        }
//                        // res.setText("Response: " + response.toString());
//                    }
////
////                   @Override
////          public void onResponse(String response) {
////            res.setText("Data : "+response);
////           }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        res.setText("Response : RESPONSE FAILED!");
//                    }
//                });
//        queue.add(jsonObjectRequest);
//        //      queue.add(stringRequest);
//    }







    private boolean checkPermissions() {
        if(ActivityCompat.checkSelfPermission(this, recordPermission) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else
        {
            ActivityCompat.requestPermissions(this,new String[]{recordPermission},PERMISSION_CODE);
            return false;
        }
    }

}