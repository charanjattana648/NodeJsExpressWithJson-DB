package com.example.charan.lab4_csis4280;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements OnEventListener<String> {

    EditText txtUserName, txtPassword;
    Button btnSignIn;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        txtUserName = findViewById(R.id.et_userName);
        txtPassword = findViewById(R.id.et_pass);
        btnSignIn = findViewById(R.id.btn_SignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fields[] = {"name", "pwd", "type"};
                String site = "http://18.208.211.255:8000";
                String fieldsValues[] = {txtUserName.getText().toString(),
                        txtPassword.getText().toString(),
                        "login"};
                intent=new Intent(MainActivity.this,PopulationDetails.class);
                intent.putExtra("site",site);
                intent.putExtra("fields",fields);
                intent.putExtra("fieldsValues",fieldsValues);
                String params = putParamsTogether(fields, fieldsValues);
                Log.d("test params", "How params look like " + params);
                new DownloadAsync(MainActivity.this).execute(site, params);
            }
        });
    }

    private String putParamsTogether(String[] fields, String... fieldValues) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            try {
                fieldValues[i] = URLEncoder.encode(fieldValues[i], "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (sb.length() > 0) {
                sb.append("&");
            }
                sb.append(fields[i]).append("=").append(fieldValues[i]);

        }
        return sb.toString();
    }

    @Override
    public void onSuccess(String result) {
        String res[]=result.split("\n");
        Log.d("test..!", "onSuccess: "+result+"--");
        Log.d("test..!!", "onSuccess: "+res[0]+"--");

        if(res[0].equalsIgnoreCase("1"))
        {
            Log.d("test.. max..!!", "onSuccess: "+res[1]+"--");
            Log.d("test.. max..!!", "onSuccess: "+res[2]+"--");
            Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT).show();
            intent.putExtra("max",Integer.parseInt(res[1]));
            String countriesName[]=res[2].split(",");
            intent.putExtra("countriesName",countriesName);
            startActivity(intent);
        }else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccessCountry(String result) {

    }

    @Override
    public void onSuccessCountryLanguage(String result) {

    }

    @Override
    public void onFailure(Exception e) {

    }
}
