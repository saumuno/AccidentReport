package com.example.accidentreport.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.accidentreport.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button loginButton;
    Button registerButton;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        switch (v.getId()){
            case R.id.loginButton:
                Toast.makeText(this,"login: user: "+ user +" pass: "+ pass,Toast.LENGTH_LONG ).show();
                break;
            case R.id.registerButton:
                Toast.makeText(this,"register: user: "+ user +" pass: "+ pass,Toast.LENGTH_LONG ).show();
                break;

        }
    }
}
